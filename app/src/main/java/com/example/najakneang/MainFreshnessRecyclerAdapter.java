package com.example.najakneang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

class MainFreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected TextView name;
    protected TextView remain;
    protected CircleImageView image;

    public MainFreshnessRecyclerHolder(@NonNull View view) {
        super(view);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class MainFreshnessRecyclerAdapter
        extends RecyclerView.Adapter<MainFreshnessRecyclerHolder> {

    private final MainFreshnessRecyclerItem[] items;

    public MainFreshnessRecyclerAdapter(MainFreshnessRecyclerItem[] data) {
        items = data;
    }

    @NonNull
    @Override
    public MainFreshnessRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new MainFreshnessRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFreshnessRecyclerHolder holder, int position) {
        MainFreshnessRecyclerItem item = items[position];
        String name = item.getName();
        int remain = item.getRemain();
        int image = item.getImage();

        holder.name.setText(name);
        holder.remain.setText(remain + "일");
        holder.image.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
