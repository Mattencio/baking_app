package com.example.bakingapp.widget;

import com.example.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesHolder {
    public static List<Recipe> sRecipes = new ArrayList<>();
    public static Recipe sSelectedRecipe;

    public static Recipe getRecipeById(int recipeId) {
        for (Recipe recipe : sRecipes) {
            if (recipe.getId() == recipeId) {
                return recipe;
            }
        }
        return null;
    }
}
