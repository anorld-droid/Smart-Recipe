package com.example.smartrecipe.adapters;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.smartrecipe.R;
import com.example.smartrecipe.models.Recipe;
import com.example.smartrecipe.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListPreloader.PreloadModelProvider<String> {

    private static final int CATEGORY_TYPE = 1;
    private static final int RECIPE_TYPE = 2;
    private static final int LOADING_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;
    private RequestManager requestManager;
    private ViewPreloadSizeProvider<String> preloadSizeProvider;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener, RequestManager requestManager, ViewPreloadSizeProvider<String> viewPreloadSizeProvider) {
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
        preloadSizeProvider = viewPreloadSizeProvider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i) {
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, onRecipeListener, requestManager);
            }
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener, requestManager, preloadSizeProvider);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }
            case EXHAUSTED_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener, requestManager, preloadSizeProvider);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipes.get(position).getCalories() == -1) {
            return CATEGORY_TYPE;
        } else if (mRecipes.get(position).getLabel().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (mRecipes.get(position).getLabel().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder)viewHolder).onBind(mRecipes.get(i));
        } else if (itemViewType == RECIPE_TYPE) {
            ((RecipeViewHolder)viewHolder).onBind(mRecipes.get(i));
        }

    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setLabel(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setCalories(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    public void displayOnlyLoading() {
        clearRecipesList();
        Recipe recipe = new Recipe();
        recipe.setLabel("LOADING...");
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (mRecipes == null) {
            mRecipes = new ArrayList<>();
        }
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setLabel("LOADING...");
            mRecipes.add(recipe);
            notifyDataSetChanged();
        }
    }

    private void clearRecipesList() {
        if (mRecipes == null) {
            mRecipes = new ArrayList<>();
        } else {
            mRecipes.clear();
        }
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if (mRecipes != null) {
            if (mRecipes.size() > 0) {
                if (mRecipes.get(mRecipes.size() - 1).getLabel().equals("LOADING...")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void hideLoading() {
        if (isLoading()) {
            if (mRecipes.get(0).getLabel().equals("LOADING...")) {
                mRecipes.remove(0);
            } else if (mRecipes.get(mRecipes.size() - 1).equals("LOADING...")) {
                mRecipes.remove(mRecipes.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    public void setQueryExhausted() {
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setLabel("EXHAUSTED...");
        mRecipes.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    public void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position) {
        if (mRecipes != null) {
            if (mRecipes.size() > 0) {
                return mRecipes.get(position);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = mRecipes.get(position).getImage();
        if (TextUtils.isEmpty(url)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(url);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item);
    }
}
