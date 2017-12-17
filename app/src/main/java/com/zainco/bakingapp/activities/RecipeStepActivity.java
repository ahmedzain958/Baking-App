package com.zainco.bakingapp.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.fragments.RecipeStepActivityFragment;

import static com.zainco.bakingapp.app.Constants.STEPINTENTEXTRA;

public class RecipeStepActivity extends AppCompatActivity {

    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            //Restore the fragment's state here
            if (getIntent().hasExtra(STEPINTENTEXTRA)) {
                RecipeStepActivityFragment recipeStepActivity = new RecipeStepActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_recipeSteps, recipeStepActivity).commit();
                Bundle bundle = getIntent().getExtras();
                recipeStepActivity.setArguments(bundle);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
