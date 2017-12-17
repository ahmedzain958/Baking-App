package com.zainco.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.adapter.RecipesAdapter;
import com.zainco.bakingapp.receiver.ConnectivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zainco.bakingapp.utils.RetrofitCall.fillRecipes;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesActivityFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    Parcelable state = null;
    boolean connected = false;

    public RecipesActivityFragment() {
    }

    @BindView(R.id.rv_recipes)
    RecyclerView rv_recipes;
    private RecipesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            state = savedInstanceState.getParcelable("rv_recipes_position");
        }
        fillRecipes(getActivity(), rv_recipes, true, state);

        return view;
    }

    @Override
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
