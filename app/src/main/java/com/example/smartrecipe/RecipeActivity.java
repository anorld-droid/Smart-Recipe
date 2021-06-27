package com.example.smartrecipe;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartrecipe.models.Recipe;

public class RecipeActivity extends com.example.smartrecipe.BaseActivity {

    private static final String TAG = "RecipeActivity";

    private AppCompatImageView mRecipeImage;
    private ImageView mRecipeBackArrow;
    private TextView mRecipeLabel, mRecipeSource,mRecipeCalories, mRecipeServings, mRecipeIngredientsTitle;
    private LinearLayout mRecipeDietList, mRecipeHealthList, mRecipeCautionsList, mRecipeIngredientsList;
    private RelativeLayout mRecipeDietContainer, mRecipeHealthContainer, mRecipeCautionsContainer;
    private ScrollView mParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeBackArrow = findViewById(R.id.back_arrow);
        mRecipeLabel = findViewById(R.id.recipe_label);
        mRecipeSource = findViewById(R.id.recipe_source);
        mRecipeCalories = findViewById(R.id.recipe_calories);
        mRecipeServings = findViewById(R.id.recipe_servings);
        mRecipeIngredientsTitle = findViewById(R.id.ingredients_title);
        mRecipeDietList = findViewById(R.id.diet_list);
        mRecipeHealthList = findViewById(R.id.health_list);
        mRecipeCautionsList = findViewById(R.id.cautions_list);
        mRecipeIngredientsList = findViewById(R.id.ingredients_list);
        mRecipeDietContainer = findViewById(R.id.diet_container);
        mRecipeHealthContainer = findViewById(R.id.health_container);
        mRecipeCautionsContainer = findViewById(R.id.cautions_container);
        mParent = findViewById(R.id.parent);

        getIncomingIntent();
        setOnClickListeners();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            setRecipeProperties(recipe);
        }
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.white_background);

            Glide.with(this)
                    .setDefaultRequestOptions(options)
                    .load(recipe.getImage())
                    .into(mRecipeImage);

            mRecipeLabel.setText(recipe.getLabel());
            mRecipeSource.setText(recipe.getSource());
            mRecipeCalories.setText(String.valueOf(Math.round(recipe.getCalories())));
            mRecipeServings.setText(String.valueOf(recipe.getYield()));
            mRecipeIngredientsTitle.setText(recipe.getIngredientLines().length + " Ingredients");

            setDietLabels(recipe.getDietLabels());
            setHealthLabels(recipe.getHealthLabels());
            setCautionsLabels(recipe.getCautions());
            setIngredients(recipe.getIngredientLines());

            showParent();
        }
    }

    private void setDietLabels(String[] dietLabels) {
        mRecipeDietList.removeAllViews();

        if (dietLabels != null) {
            if (dietLabels.length > 0) {
                for (String label : dietLabels) {
                    TextView textView = new TextView(this);
                    textView.setText(label);
                    textView.setTextSize(15);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mRecipeDietList.addView(textView);
                }
                mRecipeDietContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setHealthLabels(String[] healthLabels) {
        mRecipeHealthList.removeAllViews();

        if (healthLabels != null) {
            if (healthLabels.length > 0) {
                for (String label : healthLabels) {
                    TextView textView = new TextView(this);
                    textView.setText(label);
                    textView.setTextSize(15);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mRecipeHealthList.addView(textView);
                }
                mRecipeHealthContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setCautionsLabels(String[] cautionsLabels) {
        mRecipeCautionsList.removeAllViews();

        if (cautionsLabels != null) {
            if (cautionsLabels.length > 0) {
                for (String label : cautionsLabels) {
                    TextView textView = new TextView(this);
                    textView.setText(label);
                    textView.setTextSize(15);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mRecipeCautionsList.addView(textView);
                }
                mRecipeCautionsContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setIngredients(String[] ingredients) {
        mRecipeIngredientsList.removeAllViews();

        if (ingredients != null) {
            for (String label : ingredients) {
                TextView textView = new TextView(this);
                textView.setText("- " + label);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientsList.addView(textView);
            }
        }
    }

    private void showParent() {
        mParent.setVisibility(View.VISIBLE);
    }

    private void setOnClickListeners() {
        mRecipeBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}