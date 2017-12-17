package com.zainco.bakingapp.provider;

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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.COLUMN_RECIPE_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.COLUMN_WIDGET_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.INGREDIENT_TABLE_NAME;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.TABLE_NAME;


public class BakingContentProvider extends ContentProvider {

    public static final int RECIPES = 100;
    public static final int RECIPE_WITH_ID = 101;


    public static final int INGREDIENTS = 200;
    public static final int INGREDIENT_WITH_RECIPE_ID = 201;

    public static final int WIDGETS = 300;
    public static final int RECIPE_WITH_WIDGET_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = BakingContentProvider.class.getName();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPES + "/#", RECIPE_WITH_ID);


        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENTS + "/#", INGREDIENT_WITH_RECIPE_ID);


        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_WIDGETS, WIDGETS);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_WIDGETS + "/#", RECIPE_WITH_WIDGET_ID);


        return uriMatcher;
    }
    private BakingDbHelper mBakingDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mBakingDbHelper = new BakingDbHelper(context);
        return true;
    }

    /***
     * Handles requests to insert a single new row of data
     *
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the plants directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case RECIPES:
                // Insert new values into the database
                long id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case INGREDIENTS:
                // Insert new values into the database
                long ingredientId = db.insert(BakingContract.BakingEntry.INGREDIENT_TABLE_NAME, null, values);
                if (ingredientId > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI2, ingredientId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case WIDGETS:
                // Insert new values into the database
                long widgetId = db.insert(BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME, null, values);
                if (widgetId > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI3, widgetId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    /***
     * Handles requests for data by URI
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mBakingDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the plants directory
            case RECIPES:
                retCursor = db.query(BakingContract.BakingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.rawQuery("SELECT * " +
                        "FROM " + TABLE_NAME + " JOIN " + INGREDIENT_TABLE_NAME + " ON " + ID + " = " + COLUMN_RECIPE_ID
                        + " WHERE " + COLUMN_RECIPE_ID + " = " + id + "", null);

                break;


            case INGREDIENTS:
                retCursor = db.query(BakingContract.BakingEntry.INGREDIENT_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case INGREDIENT_WITH_RECIPE_ID:
                String recipeId = uri.getPathSegments().get(1);
                retCursor = db.query(BakingContract.BakingEntry.INGREDIENT_TABLE_NAME,
                        projection,
                        COLUMN_RECIPE_ID + "=?",
                        new String[]{recipeId},
                        null,
                        null,
                        sortOrder);
                break;
            case WIDGETS:
                retCursor = db.query(BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case RECIPE_WITH_WIDGET_ID:
                String selectedId = uri.getPathSegments().get(1);
                retCursor = db.query(BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME,
                        projection,
                        COLUMN_WIDGET_ID + "=?",
                        new String[]{selectedId},
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    /***
     * Deletes a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted plants
        int plantsDeleted; // starts as 0
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case RECIPE_WITH_ID:
                // Get the plant ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                plantsDeleted = db.delete(BakingContract.BakingEntry.TABLE_NAME, ID + "=?", new String[]{id});
                break;
            case RECIPE_WITH_WIDGET_ID:
                // Get the plant ID from the URI path
                String selectedid = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                plantsDeleted = db.delete(BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME,
                        COLUMN_RECIPE_ID + "=?", new String[]{selectedid});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver of a change and return the number of items deleted
        if (plantsDeleted != 0) {
            // A plant (or more) was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of plant deleted
        return plantsDeleted;
    }

    /***
     * Updates a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Get access to underlying database
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated plants
        int plantsUpdated;

        switch (match) {
            case RECIPES:
                plantsUpdated = db.update(BakingContract.BakingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case RECIPE_WITH_ID:
                if (selection == null) selection = BakingContract.BakingEntry.ID + "=?";
                else selection += " AND " + BakingContract.BakingEntry.ID + "=?";
                // Get the place ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Append any existing selection options to the ID filter
                if (selectionArgs == null) selectionArgs = new String[]{id};
                else {
                    ArrayList<String> selectionArgsList = new ArrayList<String>();
                    selectionArgsList.addAll(Arrays.asList(selectionArgs));
                    selectionArgsList.add(id);
                    selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);
                }
                plantsUpdated = db.update(BakingContract.BakingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items updated
        if (plantsUpdated != 0) {
            // A place (or more) was updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of places deleted
        return plantsUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
