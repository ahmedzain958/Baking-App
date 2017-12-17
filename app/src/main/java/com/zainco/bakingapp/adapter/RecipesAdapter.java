package com.zainco.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zainco.bakingapp.R;
import com.zainco.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private OnItemClickListener listener;
    private Context mContext;

    public RecipesAdapter(Context context, ArrayList<Recipe> recipes, OnItemClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
        this.mContext = context;

    }

    private Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.txt_recipe_name.setText(recipes.get(i).getName());
        if (!recipes.get(i).getImage().equals("")) {
            Glide.with(mContext)
                    .load(recipes.get(i).getImage()).error(R.drawable.exo_controls_play)
                    .into(viewHolder.img_recipe);
        } else {
            switch (i) {
                case 0:

                    viewHolder.img_recipe.setImageResource(R.drawable.nutella);
                    break;
                case 1:
                    viewHolder.img_recipe.setImageResource(R.drawable.brownies);
                    break;
                case 2:
                    viewHolder.img_recipe.setImageResource(R.drawable.yellow_cake);
                    break;
                case 3:
                    viewHolder.img_recipe.setImageResource(R.drawable.cheese_cake);
                    break;
                default:
                    viewHolder.img_recipe.setImageResource(R.drawable.cheese_cake);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_recipe_name)
        TextView txt_recipe_name;
        @BindView(R.id.img_recipe)
        ImageView img_recipe;

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
        void onClick(View item, Recipe recipe);
    }

}