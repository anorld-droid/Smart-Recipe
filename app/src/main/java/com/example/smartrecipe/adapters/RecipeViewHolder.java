package com.example.smartrecipe.adapters;




import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.smartrecipe.R;
import com.example.smartrecipe.models.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView label, source, calories;
    AppCompatImageView image;
    OnRecipeListener onRecipeListener;
    RequestManager requestManager;

    ViewPreloadSizeProvider viewPreloadSizeProvider;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager, ViewPreloadSizeProvider preloadSizeProvider) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
        viewPreloadSizeProvider = preloadSizeProvider;

        label = itemView.findViewById(R.id.recipe_label);
        source = itemView.findViewById(R.id.recipe_source);
        calories = itemView.findViewById(R.id.recipe_calories);
        image = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe) {
        requestManager
                .load(recipe.getImage())
                .into(image);

        label.setText(recipe.getLabel());
        source.setText(recipe.getSource());
        calories.setText(String.valueOf(Math.round(recipe.getCalories())));
        viewPreloadSizeProvider.setView(image);
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
