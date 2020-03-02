package com.example.bakingapp.widget.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.bakingapp.widget.RecipesWidgetProvider;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.widget.RecipesHolder;

import androidx.annotation.Nullable;

public class WidgetRecipeService extends IntentService {
    public static final String ACTION_SELECT_RECIPE = "select_recipe";
    private static final String ACTION_RETURN_RECIPES_LIST = "return_recipes_list";
    private static final String ACTION_SELECT_RECIPE_WITH_ID = "select_recipe_with_id";

    public WidgetRecipeService() {
        super(WidgetRecipeService.class.getSimpleName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetRecipeService(String name) {
        super(name);
    }

    public static Intent getFillInIntent(Integer recipeId) {
        Bundle extras = new Bundle();
        extras.putInt(WidgetRecipeService.ACTION_SELECT_RECIPE_WITH_ID, recipeId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        return fillInIntent;
    }

    public static PendingIntent getPendingIntentTemplate(Context context){
        Intent intent = new Intent(context, WidgetRecipeService.class);
        intent.setAction(WidgetRecipeService.ACTION_SELECT_RECIPE);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();

        if (ACTION_SELECT_RECIPE.equals(action)) {
            recipeSelected(intent);
        }
        else if (ACTION_RETURN_RECIPES_LIST.equals(action)) {
            returnToRecipesList();
        }
    }

    private void returnToRecipesList() {
        RecipesWidgetProvider.updateAppWidget(this);
    }

    private void recipeSelected(Intent intent) {

        int recipeId = intent.getIntExtra(ACTION_SELECT_RECIPE_WITH_ID, -1);
        Recipe recipe = RecipesHolder.getRecipeById(recipeId);

        RecipesWidgetProvider.showRecipeIngredients(this, recipe);
    }

    public static Intent returnToRecipesListIntent(Context context) {
        Intent intent = new Intent(context, WidgetRecipeService.class);
        intent.setAction(WidgetRecipeService.ACTION_RETURN_RECIPES_LIST);
        return intent;
    }
}
