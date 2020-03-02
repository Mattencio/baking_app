package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.retrofit.RetrofitManager;
import com.example.bakingapp.widget.services.WidgetIngredientsListService;
import com.example.bakingapp.widget.services.WidgetRecipeListService;
import com.example.bakingapp.widget.services.WidgetRecipeService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, getRecipesList(context));
    }

    public static void updateAppWidget(Context context) {
        // Instruct the widget manager to update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidgetProvider.class));
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, getRecipesList(context));
        }
    }

    public static void showRecipeIngredients(Context context, Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        // Instruct the widget manager to update the widget

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidgetProvider.class));
        for (int appWidgetIdSingle : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetIdSingle, getIngredientListRemoteView(context, recipe));
        }
    }

    private static RemoteViews getIngredientListRemoteView(Context context, Recipe recipe){
        RecipesHolder.sSelectedRecipe = recipe;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list);
        Intent intent = new Intent(context, WidgetIngredientsListService.class);
        views.setRemoteAdapter(R.id.gv_widget_ingredients, intent);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, WidgetRecipeService.returnToRecipesListIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ib_return, pendingIntent);

        return views;
    }

    private static RemoteViews getRecipesList(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget_provider);
        Intent intent = new Intent(context, WidgetRecipeListService.class);
        views.setRemoteAdapter(R.id.gv_widget_recipes, intent);
        views.setPendingIntentTemplate(R.id.gv_widget_recipes, WidgetRecipeService.getPendingIntentTemplate(context));
        views.setEmptyView(R.id.gv_widget_recipes, R.id.empty_view);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        new RetrofitManager().getRecipes(new RetrofitManager.RecipesListener() {
            @Override
            public void onRecipesReceived(List<Recipe> recipes) {
                RecipesHolder.sRecipes = recipes;
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget_recipes);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static PendingIntent getPendingIntentTemplate(Context context){
        Intent intent = new Intent(context, WidgetRecipeService.class);
        intent.setAction(WidgetRecipeService.ACTION_SELECT_RECIPE);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

