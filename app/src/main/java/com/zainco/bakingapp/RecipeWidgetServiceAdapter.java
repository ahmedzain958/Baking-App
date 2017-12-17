package com.zainco.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zainco.bakingapp.provider.BakingContract;

import static com.zainco.bakingapp.FillRecipesService.EXTRA_RECIPE_ID;
import static com.zainco.bakingapp.provider.BakingContract.BASE_CONTENT_URI;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;
import static com.zainco.bakingapp.provider.BakingContract.PATH_RECIPES;

/**
 * Created by Zain on 09/12/2017.
 */

public class RecipeWidgetServiceAdapter extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        mCursor = mContext.getContentResolver().query(
                URI,
                null,
                null,
                null,
                ID);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null)
            mCursor.close();

    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(ID);
        int nameIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);

        int recipeId = mCursor.getInt(idIndex);
        String recipeName = mCursor.getString(nameIndex);


        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.card_row_wiget);


        switch (position) {
            case 0:
                views.setImageViewResource(R.id.img_recipe, R.drawable.nutella);
                break;
            case 1:
                views.setImageViewResource(R.id.img_recipe, R.drawable.brownies);
                break;
            case 2:
                views.setImageViewResource(R.id.img_recipe, R.drawable.yellow_cake);
                break;
            case 3:
                views.setImageViewResource(R.id.img_recipe, R.drawable.cheese_cake);
                break;
            default:
                views.setImageViewResource(R.id.img_recipe, R.drawable.cheese_cake);
                break;
        }
        views.setTextViewText(R.id.txt_recipe_name, recipeName);
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_RECIPE_ID, recipeId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.img_recipe, fillInIntent);

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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
