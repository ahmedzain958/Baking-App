package com.zainco.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredients;
    private OnItemClickListener listener;

    public IngredientsAdapter(ArrayList<Ingredient> ingredients, OnItemClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;

    }

    private Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_ingredient, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder viewHolder, int i) {
        Ingredient ingredient = ingredients.get(i);
        viewHolder.txt_quantity.setText("Quantity:" + ingredient.getQuantity().toString());
        viewHolder.txt_ingredient.setText("Ingredient:" + ingredient.getIngredient().toString());
        viewHolder.txt_measure.setText("Measure:" + ingredient.getMeasure().toString());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_measure)
        TextView txt_measure;
        @BindView(R.id.txt_ingredient)
        TextView txt_ingredient;

        public ViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(view, getItem(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(View item, Ingredient ingredient);
    }

}