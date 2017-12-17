package com.zainco.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Zain on 08/12/2017.
 */

public class BakingContract {
    public static final String AUTHORITY = "com.zainco.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    public static final String PATH_WIDGETS = "widgets";


    public static final class BakingEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final Uri CONTENT_URI2 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final Uri CONTENT_URI3 =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WIDGETS).build();


        public static final String TABLE_NAME = "recipes";
        public static final String ID = "Id";
        public static final String COLUMN_RECIPE_NAME = "name";


        public static final String INGREDIENT_TABLE_NAME = "ingredients";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

        public static final String SELECTED_RECIPES_TABLE_NAME = "selected_recipes";
        public static final String COLUMN_WIDGET_ID = "widgetId";
    }
}
