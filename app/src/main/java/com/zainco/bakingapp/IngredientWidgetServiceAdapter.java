package com.zainco.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zainco.bakingapp.model.Ingredient;
import com.zainco.bakingapp.provider.BakingContract;

import static com.zainco.bakingapp.FillRecipesService.EXTRA_RECIPE_WIDGET_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.CONTENT_URI2;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.CONTENT_URI3;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;
import static com.zainco.bakingapp.utils.Library.getIngredient;

/**
 * Created by Zain on 10/12/2017.
 */

public class IngredientWidgetServiceAdapter extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext(),
                intent.getIntExtra(EXTRA_RECIPE_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID));
    }
}


class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor widgetsCursor;
    private int widgetId;

    public IngredientsRemoteViewsFactory(Context applicationContext, int widgetId) {
        mContext = applicationContext;
        this.widgetId = widgetId;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        Cursor widgetsCursor1 = mContext.getContentResolver().query(Uri.withAppendedPath(CONTENT_URI3, String.valueOf(widgetId)),
                null,
                null,
                null,
                ID);
        if (widgetsCursor1 != null && widgetsCursor1.getCount() > 0) {
            int idIndex = widgetsCursor1.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_ID);
            int recipeId = 0;
            while (widgetsCursor1.moveToNext()) {
                recipeId = widgetsCursor1.getInt(idIndex);
                break;
            }
            widgetsCursor1.close();
            widgetsCursor = mContext.getContentResolver().query(Uri.withAppendedPath(CONTENT_URI2, String.valueOf(recipeId)),
                    null,
                    null,
                    null, ID);
        }

    }

    @Override
    public void onDestroy() {
        if (widgetsCursor != null)
            widgetsCursor.close();
    }

    @Override
    public int getCount() {
        if (widgetsCursor == null) return 0;
        return widgetsCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (widgetsCursor == null || widgetsCursor.getCount() == 0) return null;
        widgetsCursor.moveToPosition(position);


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.wiget_ingredients);

        Ingredient ingredient = getIngredient(widgetsCursor);
        views.setTextViewText(R.id.txt_quantity, "Quantity: " + ingredient.getQuantity().toString());
        views.setTextViewText(R.id.txt_measure, "Measure: " + ingredient.getMeasure().toString());
        views.setTextViewText(R.id.txt_ingredient, "Ingredient: " + ingredient.getIngredient().toString());

        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.txt_quantity, fillInIntent);
        return views;

    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}