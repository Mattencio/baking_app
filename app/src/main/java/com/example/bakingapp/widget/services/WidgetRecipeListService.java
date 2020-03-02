package com.example.bakingapp.widget.services;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.widget.RecipesHolder;

public class WidgetRecipeListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipesGridRemoteViewsFactory(getApplicationContext());
    }
}

class RecipesGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    RecipesGridRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        RecipesHolder.sRecipes = null;
    }

    @Override
    public int getCount() {
        return RecipesHolder.sRecipes == null ? 0 : RecipesHolder.sRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (RecipesHolder.sRecipes == null || RecipesHolder.sRecipes.isEmpty()) {
            return null;
        }

        Recipe recipe = RecipesHolder.sRecipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_item_widget);

        views.setTextViewText(R.id.tv_recipe_name_widget, recipe.getName());

        views.setOnClickFillInIntent(R.id.ll_recipe_item, WidgetRecipeService.getFillInIntent(recipe.getId()));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
