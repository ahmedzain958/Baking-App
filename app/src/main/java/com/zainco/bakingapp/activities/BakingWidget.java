package com.zainco.bakingapp.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.zainco.bakingapp.FillRecipesService;
import com.zainco.bakingapp.IngredientWidgetServiceAdapter;
import com.zainco.bakingapp.R;
import com.zainco.bakingapp.RecipeWidgetServiceAdapter;

import static com.zainco.bakingapp.FillRecipesService.ACTION_FILL_INGREDIENTS;
import static com.zainco.bakingapp.FillRecipesService.EXTRA_RECIPE_ID;
import static com.zainco.bakingapp.FillRecipesService.EXTRA_RECIPE_WIDGET_ID;
import static com.zainco.bakingapp.FillRecipesService.startActionFillRecipes;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        startActionFillRecipes(context, appWidgetId);
    }

    public static void updateRecipesWidgets(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        Intent intent = new Intent(context, RecipeWidgetServiceAdapter.class);
        views.setRemoteAdapter(R.id.lv, intent);

        Intent recipeClicked = new Intent(context, FillRecipesService.class);
        recipeClicked.setAction(ACTION_FILL_INGREDIENTS);
        recipeClicked.putExtra(FillRecipesService.EXTRA_RECIPE_WIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, recipeClicked,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.lv, pendingIntent);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    public static void updateIngredientWidgets(Context context, int recipeId, AppWidgetManager appWidgetManager, int widgetId) {
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.lv);
        RemoteViews views =new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        Intent intent = new Intent(context, IngredientWidgetServiceAdapter.class);
        intent.putExtra(EXTRA_RECIPE_WIDGET_ID, widgetId);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.lv, intent);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        super.onDeleted(context, appWidgetIds);
    }
}

