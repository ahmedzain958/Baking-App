package com.zainco.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.activities.RecipeStepActivity;
import com.zainco.bakingapp.adapter.IngredientsAdapter;
import com.zainco.bakingapp.adapter.StepsAdapter;
import com.zainco.bakingapp.model.Ingredient;
import com.zainco.bakingapp.model.Recipe;
import com.zainco.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zainco.bakingapp.app.Constants.RECIPE_INTENT_EXTRA;
import static com.zainco.bakingapp.app.Constants.STEPINTENTEXTRA;
import static com.zainco.bakingapp.utils.Library.isTablet;

public class DetailActivityFragment extends Fragment {
    @BindView(R.id.rv_ingredients)
    RecyclerView rv_ingredients;
    @BindView(R.id.rv_stepDescriptions)
    RecyclerView rv_stepDescriptions;


    @BindView(R.id.tv_recipeName)
    TextView tv_recipeName;
    Parcelable rv_ingredients_state = null;
    Parcelable rv_stepDescriptions_state = null;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Recipe recipe = bundle.getParcelable(RECIPE_INTENT_EXTRA);
            if (recipe != null) {
                tv_recipeName.setText(recipe.getName());
                List<Ingredient> jsonIngredients = recipe.getIngredients();
                ArrayList<Ingredient> ingredients = new ArrayList<>(jsonIngredients.size());
                ingredients.addAll(jsonIngredients);
                setState(savedInstanceState);


                if (ingredients != null && ingredients.size() > 0) {
                    List<Step> jsonSteps = recipe.getSteps();
                    if (jsonSteps != null) {

                        ArrayList<Step> steps = new ArrayList<>(jsonSteps.size());
                        steps.addAll(jsonSteps);
                        if (steps != null && steps.size() > 0) {
                            rv_ingredients.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            if (rv_ingredients_state != null)
                                rv_ingredients.getLayoutManager().onRestoreInstanceState(rv_ingredients_state);
                            rv_ingredients.setAdapter(new IngredientsAdapter(ingredients, new IngredientsAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(View item, Ingredient recipe) {
                                    Toast.makeText(getActivity(), String.valueOf(recipe.getQuantity()) + "-" +
                                            recipe.getMeasure() + "-" + recipe.getIngredient(), Toast.LENGTH_SHORT).show();
                                }
                            }));


                            rv_stepDescriptions.setLayoutManager(new LinearLayoutManager(getActivity()));
                            if (rv_stepDescriptions_state != null)
                                rv_stepDescriptions.getLayoutManager().onRestoreInstanceState(rv_stepDescriptions_state);
                            rv_stepDescriptions.setAdapter(new StepsAdapter(steps, new StepsAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(View item, Step step) {
                                    if (!isTablet(getActivity())) {
                                        startActivity(new Intent(getActivity(), RecipeStepActivity.class)
                                                .putExtra(STEPINTENTEXTRA, step));
                                    } else {
                                        RecipeStepActivityFragment recipeStepActivityFragment = new RecipeStepActivityFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(STEPINTENTEXTRA, step);
                                        recipeStepActivityFragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frag_recipeDetail2, recipeStepActivityFragment).commit();
                                    }
                                }
                            }));
                        }
                    }

                }

            }
        }
        return view;
    }

    private void setState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            rv_ingredients_state = savedInstanceState.getParcelable("rv_ingredients_position");
            rv_stepDescriptions_state = savedInstanceState.getParcelable("rv_stepDescriptions_position");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("rv_ingredients_position", rv_ingredients.getLayoutManager().onSaveInstanceState());
        savedInstanceState.putParcelable("rv_stepDescriptions_position", rv_stepDescriptions.getLayoutManager().onSaveInstanceState());

        super.onSaveInstanceState(savedInstanceState);
    }
}
