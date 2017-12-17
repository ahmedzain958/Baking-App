package com.zainco.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.fragments.DetailActivityFragment;
import com.zainco.bakingapp.fragments.RecipeStepActivityFragment;

import static com.zainco.bakingapp.app.Constants.RECIPE_INTENT_EXTRA;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            if (getIntent().hasExtra(RECIPE_INTENT_EXTRA)) {
                if (findViewById(R.id.frag_recipeDetail2) != null) {//tablet
                    RecipeStepActivityFragment recipeStepActivityFragment = new RecipeStepActivityFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frag_recipeDetail2, recipeStepActivityFragment).commit();
                }
                DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_recipeDetail, detailActivityFragment).commit();

                Bundle bundle = getIntent().getExtras();
                detailActivityFragment.setArguments(bundle);
            }
        }
    }

}
