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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.COLUMN_RECIPE_ID;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;


public class BakingDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "baking.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the plants data
        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + BakingContract.BakingEntry.TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.BakingEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL)";

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + BakingContract.BakingEntry.INGREDIENT_TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_RECIPE_ID + " INTEGER ," +
                BakingContract.BakingEntry.COLUMN_QUANTITY + " TEXT NOT NULL," +
                BakingContract.BakingEntry.COLUMN_INGREDIENT + " TEXT NOT NULL," +
                BakingContract.BakingEntry.COLUMN_MEASURE + " TEXT NOT NULL)";

        final String SQL_CREATE_SELECTED_RECIPES_TABLE = "CREATE TABLE " + BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME
                + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_RECIPE_ID + " INTEGER ," +
                BakingContract.BakingEntry.COLUMN_WIDGET_ID + " INTEGER)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SELECTED_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.INGREDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.SELECTED_RECIPES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
