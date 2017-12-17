package com.zainco.bakingapp;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.zainco.bakingapp.provider.BakingContract;

import static com.zainco.bakingapp.activities.BakingWidget.updateIngredientWidgets;
import static com.zainco.bakingapp.activities.BakingWidget.updateRecipesWidgets;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.COLUMN_RECIPE_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.COLUMN_WIDGET_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.CONTENT_URI3;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FillRecipesService extends IntentService {

    public static final String ACTION_FILL_RECIPE = "com.zainco.bakingapp.action.fillrecipe";
    public static final String ACTION_FILL_INGREDIENTS = "com.zainco.bakingapp.action.fillingredients";
    public static final String EXTRA_RECIPE_ID = "com.example.android.mygarden.extra.RECIPE_ID";
    public static final String EXTRA_RECIPE_WIDGET_ID = "com.example.android.mygarden.extra.RECIPE_WIDGET_ID";

    public FillRecipesService() {
        super("FillRecipe");
    }

    public static void startActionFillRecipes(Context context, int appWidgetId) {
        Intent intent = new Intent(context, FillRecipesService.class);
        intent.setAction(ACTION_FILL_RECIPE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FILL_INGREDIENTS.equals(action)) {
                int recipeId = intent.getExtras().getInt(EXTRA_RECIPE_ID);
                int widgetId = intent.getExtras().getInt(EXTRA_RECIPE_WIDGET_ID);
                insertWidget(recipeId, widgetId);
                handleActionfillIngredient(recipeId, widgetId);
            } else if (ACTION_FILL_RECIPE.equals(action)) {
                int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                handleActionfillRecipe(widgetId);
            }
        }
    }

    private void insertWidget(int recipeId, int widgetId) {
        Uri URI3 = CONTENT_URI3;
        Cursor widgetsCursor = getContentResolver().query(
                URI3,
                null,
                COLUMN_RECIPE_ID + " = ? AND " + COLUMN_WIDGET_ID + " = ?",
                new String[]{String.valueOf(recipeId), String.valueOf(widgetId)},
                ID);

        if (widgetsCursor != null && widgetsCursor.getCount() > 0){
            widgetsCursor.close();
            return;
        }
        widgetsCursor.close();
        ContentValues widgetContentValues = new ContentValues();
        widgetContentValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_ID, recipeId);
        widgetContentValues.put(BakingContract.BakingEntry.COLUMN_WIDGET_ID, widgetId);
        Uri uri = CONTENT_URI3;
        getContentResolver().insert(uri, widgetContentValues);

    }

    private void handleActionfillIngredient(int recipeId, int widgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        updateIngredientWidgets(this, recipeId, appWidgetManager, widgetId);
    }

    private void handleActionfillRecipe(int widgetId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        updateRecipesWidgets(this, appWidgetManager, widgetId);
    }


}
