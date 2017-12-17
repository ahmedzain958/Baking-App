package com.zainco.bakingapp.network;

import com.zainco.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesService {
    @GET("baking.json")
     Call<ArrayList<Recipe>> getRecipes();
}
