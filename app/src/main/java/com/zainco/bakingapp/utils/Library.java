package com.zainco.bakingapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;

import com.zainco.bakingapp.model.Ingredient;
import com.zainco.bakingapp.provider.BakingContract;

import java.util.List;

/**
 * Created by Zain on 29/11/2017.
 */

public class Library {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static Ingredient getIngredient(Cursor cursor) {
        int quantityIndex = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_QUANTITY);
        int measureIndex = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_MEASURE);
        int ingredientIndex = cursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_INGREDIENT);


        String quantityStr = cursor.getString(quantityIndex);
        String measure = cursor.getString(measureIndex);
        String ingredient = cursor.getString(ingredientIndex);

        Double quantity = 0.0;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
        }

        return new Ingredient(quantity, measure, ingredient);
    }


    public static void insertIngredients(final Context context, List<Ingredient> ingredients, int recipeId) {


    }
}
