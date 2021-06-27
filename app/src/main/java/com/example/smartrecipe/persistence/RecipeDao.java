package com.example.smartrecipe.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.smartrecipe.models.Recipe;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface RecipeDao {

    @Insert(onConflict = IGNORE)
    long[] insertRecipes(Recipe... recipe);

    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    @Query("UPDATE recipes SET label = :label, image = :image, source = :source, calories = :calories, queryFlag = :queryFlag WHERE uri = :uri")
    void updateRecipe(String uri, String label, String image, String source, float calories, String queryFlag);

    @Query("SELECT * FROM recipes WHERE queryFlag = :query LIMIT (:pageNumber * 29)")
    LiveData<List<Recipe>> searchRecipes(String query, int pageNumber);
}
