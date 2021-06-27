package com.example.smartrecipe.util;


import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.smartrecipe.AppExecutors;
import com.example.smartrecipe.requests.responses.ApiResponse;

public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();
    private AppExecutors appExecutors;

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init() {
        results.setValue((Resource<CacheObject>)Resource.loading(null));

        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {
                results.removeSource(dbSource);

                if (shouldFetch(cacheObject)) {
                    fetchFromNetwork(dbSource);
                } else {
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        Log.d(TAG, "fetchFromNetwork: called.");

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    Log.d(TAG, "onChanged: ApiSuccessResponse.");
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            saveCallResult((RequestObject)processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));

                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(@Nullable CacheObject cacheObject) {
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    Log.d(TAG, "onChanged: ApiEmptyResponse.");
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(@Nullable CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    Log.d(TAG, "onChanged: ApiErrorResponse.");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.error(((ApiResponse.ApiErrorResponse)requestObjectApiResponse).getErrorMessage(), cacheObject));
                        }
                    });
                }
            }
        });
    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response) {
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }

}
