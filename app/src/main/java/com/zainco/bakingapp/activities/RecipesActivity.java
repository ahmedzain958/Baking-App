package com.zainco.bakingapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.app.MyApplication;
import com.zainco.bakingapp.fragments.RecipesActivityFragment;
import com.zainco.bakingapp.receiver.ConnectivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zainco.bakingapp.utils.RetrofitCall.fillRecipes;

public class RecipesActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    @BindView(R.id.rv_recipes)
    RecyclerView rv_recipes;
    private Parcelable state = null;

    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {

            state = savedInstanceState.getParcelable("rv_recipes_position");


        }
        if (findViewById(R.id.frag_recipes) != null) {//tablet
            RecipesActivityFragment recipesActivityFragment = new RecipesActivityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_recipes, recipesActivityFragment).commit();
        } else {//no tablet
            ButterKnife.bind(this);
            fillRecipes(RecipesActivity.this, rv_recipes, false, state);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (rv_recipes != null && connected) {
            savedInstanceState.putParcelable("rv_recipes_position", rv_recipes.getLayoutManager().onSaveInstanceState());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        connected = isConnected;
    }
}
