package com.example.smartrecipe.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipes implements Parcelable {

    private Recipe recipe;

    public Recipes(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipes() {
    }

    protected Recipes(Parcel in) {
        recipe = in.readParcelable(Recipe.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(recipe, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Recipes{" +
                "recipe=" + recipe +
                '}';
    }
}
