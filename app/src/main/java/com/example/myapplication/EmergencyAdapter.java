package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    Context context;
    ArrayList<String> titles, descriptions, categories, icons;

    public EmergencyAdapter(Context ctx, ArrayList<String> titles, ArrayList<String> descriptions, 
                           ArrayList<String> categories, ArrayList<String> icons) {
        this.context = ctx;
        this.titles = titles;
        this.descriptions = descriptions;
        this.categories = categories;
        this.icons = icons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emergency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));
        holder.category.setText(categories.get(position));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("title", titles.get(position));
            intent.putExtra("details", descriptions.get(position));
            intent.putExtra("category", categories.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, category;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.descriptionText);
            category = itemView.findViewById(R.id.categoryText);
        }
    }
}
