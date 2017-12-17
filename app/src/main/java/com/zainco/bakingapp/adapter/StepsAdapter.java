package com.zainco.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zainco.bakingapp.R;
import com.zainco.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private ArrayList<Step> steps;
    private OnItemClickListener listener;

    public StepsAdapter(ArrayList<Step> steps, OnItemClickListener listener) {
        this.steps = steps;
        this.listener = listener;

    }

    private Step getItem(int position) {
        return steps.get(position);
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_step, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.ViewHolder viewHolder, int i) {
        Step step = steps.get(i);
        viewHolder.txt_shortDescription.setText( step.getShortDescription().toString());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_shortDescription)
        TextView txt_shortDescription;


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
        void onClick(View item, Step step);
    }

}