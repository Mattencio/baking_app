package com.example.bakingapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipesListViewHolder> {

    private List<Recipe> mRecipes;
    private OnRecipeClickListener mRecipeClickListener;

    public interface OnRecipeClickListener {
        void OnRecipeClicked(Recipe recipe);
    }

    public RecipesListAdapter(List<Recipe> recipes, OnRecipeClickListener onRecipeClickListener) {
        mRecipes = recipes;
        mRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecipesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdItem = R.layout.item_recipe;
        View view = inflater.inflate(layoutIdItem, parent, false);
        return new RecipesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesListViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.bindRecipe(recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class RecipesListViewHolder extends RecyclerView.ViewHolder {
        private Recipe mRecipe;

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;

        @OnClick(R.id.cv_recipe_item)
        void recipeClickListener() {
            mRecipeClickListener.OnRecipeClicked(mRecipe);
        }

        RecipesListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindRecipe(Recipe recipe) {
            String recipeName = recipe.getName();
            mRecipeName.setText(recipeName);
            mRecipe = recipe;
        }
    }
}
