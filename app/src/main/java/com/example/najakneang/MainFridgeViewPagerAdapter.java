package com.example.najakneang;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MainFridgeViewPagerHolder extends RecyclerView.ViewHolder {
    protected TextView text;

    public MainFridgeViewPagerHolder(@NonNull View view) {
        super(view);
        this.text = view.findViewById(R.id.text_item_fridge_main);
    }
}

public class MainFridgeViewPagerAdapter
        extends RecyclerView.Adapter<MainFridgeViewPagerHolder> {

    private String[] items;

    public MainFridgeViewPagerAdapter(String[] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MainFridgeViewPagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_viewpager_fridge_main, viewGroup, false);

        return new MainFridgeViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFridgeViewPagerHolder holder, int position) {
        holder.text.setText(items[position]);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FridgeActivity.class);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
