package com.example.smartrecipe.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    @NonNull
    private String uri;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "source")
    private String source;

    @ColumnInfo(name = "yield")
    private int yield;

    @ColumnInfo(name = "dietLabels")
    private String[] dietLabels;

    @ColumnInfo(name = "healthLabels")
    private String[] healthLabels;

    @ColumnInfo(name = "cautions")
    private String[] cautions;

    @ColumnInfo(name = "ingredientLines")
    private String[] ingredientLines;

    @ColumnInfo(name = "calories")
    private float calories;

    @ColumnInfo(name = "queryFlag")
    private String queryFlag;

    public Recipe(String uri, String label, String image, String source, int yield, String[] dietLabels, String[] healthLabels, String[] cautions, String[] ingredientLines, float calories, String queryFlag) {
        this.uri = uri;
        this.label = label;
        this.image = image;
        this.source = source;
        this.yield = yield;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.cautions = cautions;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.queryFlag = queryFlag;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        uri = in.readString();
        label = in.readString();
        image = in.readString();
        source = in.readString();
        yield = in.readInt();
        dietLabels = in.createStringArray();
        healthLabels = in.createStringArray();
        cautions = in.createStringArray();
        ingredientLines = in.createStringArray();
        calories = in.readFloat();
        queryFlag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(label);
        dest.writeString(image);
        dest.writeString(source);
        dest.writeInt(yield);
        dest.writeStringArray(dietLabels);
        dest.writeStringArray(healthLabels);
        dest.writeStringArray(cautions);
        dest.writeStringArray(ingredientLines);
        dest.writeFloat(calories);
        dest.writeString(queryFlag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    //getters and setters
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(String[] dietLabels) {
        this.dietLabels = dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(String[] healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String[] getCautions() {
        return cautions;
    }

    public void setCautions(String[] cautions) {
        this.cautions = cautions;
    }

    public String[] getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(String[] ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public float getCalories() {
        if (calories != -1) {
            if (calories != 0 && yield != 0) {
                return calories / yield;
            }
        }
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(String queryFlag) {
        this.queryFlag = queryFlag;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uri='" + uri + '\'' +
                ", label='" + label + '\'' +
                ", image='" + image + '\'' +
                ", source='" + source + '\'' +
                ", yield=" + yield +
                ", dietLabels=" + Arrays.toString(dietLabels) +
                ", healthLabels=" + Arrays.toString(healthLabels) +
                ", cautions=" + Arrays.toString(cautions) +
                ", ingredientLines=" + Arrays.toString(ingredientLines) +
                ", calories=" + calories +
                ", queryFlag='" + queryFlag + '\'' +
                '}';
    }
}
