package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private final List<DetailsActivity.Step> steps;

    public StepsAdapter(List<DetailsActivity.Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        DetailsActivity.Step step = steps.get(position);
        holder.stepNumber.setText(String.valueOf(step.number));
        holder.stepDescription.setText(step.description);
        holder.stepImage.setImageResource(step.imageResId);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        ImageView stepImage;
        TextView stepNumber, stepDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            stepImage = itemView.findViewById(R.id.stepImage);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepDescription = itemView.findViewById(R.id.stepDescription);
        }
    }
} 