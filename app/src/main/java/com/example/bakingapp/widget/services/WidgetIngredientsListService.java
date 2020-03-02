package com.example.bakingapp.widget.services;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.widget.RecipesHolder;

import java.util.List;

public class WidgetIngredientsListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsGridRemoteViewsFactory(getApplicationContext());
    }
}

class IngredientsGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients;

    IngredientsGridRemoteViewsFactory(Context context) {
        mContext = context;
        mIngredients = RecipesHolder.sSelectedRecipe.getIngredients();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        RecipesHolder.sSelectedRecipe = null;
    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.isEmpty()) {
            return null;
        }

        Ingredient ingredient = mIngredients.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item_widget);

        String ingredientText = "- " + ingredient.getIngredientName();
        views.setTextViewText(R.id.tv_ingredient_name_widget, ingredientText);
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
