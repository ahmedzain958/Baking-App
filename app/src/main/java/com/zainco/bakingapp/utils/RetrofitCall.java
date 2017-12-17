package com.zainco.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zainco.bakingapp.activities.DetailActivity;
import com.zainco.bakingapp.adapter.RecipesAdapter;
import com.zainco.bakingapp.model.Ingredient;
import com.zainco.bakingapp.model.Recipe;
import com.zainco.bakingapp.network.RecipesService;
import com.zainco.bakingapp.provider.BakingContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zainco.bakingapp.app.Constants.BASEURL;
import static com.zainco.bakingapp.app.Constants.RECIPE_INTENT_EXTRA;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.CONTENT_URI;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.CONTENT_URI2;
import static com.zainco.bakingapp.provider.BakingContract.BakingEntry.ID;

/**
 * Created by Zain on 27/11/2017.
 */

public class RetrofitCall {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void fillRecipes(final Context context, final RecyclerView recyclerView, final boolean isTablet,
                                   final Parcelable state) {

        if (!isNetworkAvailable(context)) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipesService service = retrofit.create(RecipesService.class);

        Call<ArrayList<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                             ArrayList<Recipe> recipeData = response.body();
                             if (recipeData != null) {
                                 boolean existing = existRecipes(context);
                                 if (!existing) {
                                     for (Recipe recipe : recipeData) {
                                         int recipeId = recipe.getId();
                                         insertRecipes(context, recipe);
                                         boolean existIng = existIngredients(context, recipeId);
                                         if (!existIng) {
                                             insertIngredients(context, recipe);
                                         }
                                     }
                                 }
                                 recyclerView.setLayoutManager(isTablet ? (
                                         new GridLayoutManager(context, 2)) :
                                         (new LinearLayoutManager(context)));

                                 if (state !=null) recyclerView.getLayoutManager().onRestoreInstanceState(state);

                                 recyclerView.setAdapter(new RecipesAdapter(context,recipeData, new RecipesAdapter.OnItemClickListener() {
                                     @Override
                                     public void onClick(View item, Recipe recipe) {
                                         if (recipe != null) {
                                             context.startActivity(new Intent(context, DetailActivity.class)
                                                     .putExtra(RECIPE_INTENT_EXTRA, recipe));
                                         }
                                     }
                                 }));

                             }
                         }


                         @Override
                         public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                             Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                         }
                     }

        );
    }

    private static void insertRecipes(final Context context, Recipe recipe) {
        ContentValues contentValues = new ContentValues();
        int recipeId = recipe.getId();
        contentValues.put(BakingContract.BakingEntry.ID, recipeId);
        contentValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_NAME, recipe.getName());
        context.getContentResolver().insert(CONTENT_URI, contentValues);

    }

    private static void insertIngredients(final Context context, Recipe recipe) {

        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null && ingredients.size() > 0) {
            for (Ingredient ingredient : ingredients) {
                ContentValues ingredientContentValues = new ContentValues();
                ingredientContentValues.put(BakingContract.BakingEntry.COLUMN_RECIPE_ID, recipe.getId());
                ingredientContentValues.put(BakingContract.BakingEntry.COLUMN_QUANTITY, ingredient.getQuantity());
                ingredientContentValues.put(BakingContract.BakingEntry.COLUMN_MEASURE, ingredient.getMeasure());
                ingredientContentValues.put(BakingContract.BakingEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
                context.getContentResolver().insert(CONTENT_URI2, ingredientContentValues);
            }
        }

    }


    public static boolean existRecipes(Context context) {
        Uri URI = CONTENT_URI;
        Cursor mCursor = context.getContentResolver().query(
                URI,
                null,
                null,
                null,
                ID);


        if (mCursor == null || mCursor.getCount() == 0) {
            mCursor.close();
            return false;
        }
        mCursor.close();
        return true;
    }


    public static boolean existIngredients(Context context, int recipeId) {
        Cursor mCursor = context.getContentResolver().query(
                Uri.withAppendedPath(CONTENT_URI2, String.valueOf(recipeId)),
                null,
                null,
                null,
                ID);


        if (mCursor == null || mCursor.getCount() == 0) {
            mCursor.close();
            return false;
        }
        mCursor.close();
        return true;
    }
}
