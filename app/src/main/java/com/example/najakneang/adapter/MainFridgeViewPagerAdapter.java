package com.example.najakneang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;

class MainFridgeViewPagerHolder extends RecyclerView.ViewHolder {
    protected final TextView text;

    public MainFridgeViewPagerHolder(@NonNull View view) {
        super(view);
        this.text = view.findViewById(R.id.text_item_fridge_main);
    }
}

public class MainFridgeViewPagerAdapter
        extends RecyclerView.Adapter<MainFridgeViewPagerHolder> {

    private final String[] data;

    public MainFridgeViewPagerAdapter(String[] data) {
        this.data = data;
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
        holder.text.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
