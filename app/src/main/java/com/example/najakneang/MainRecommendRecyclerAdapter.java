package com.example.najakneang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MainRecommendRecyclerHolder extends RecyclerView.ViewHolder {
    protected TextView title;
    protected TextView creator;
    protected ImageView thumbnail;

    public MainRecommendRecyclerHolder(@NonNull View view) {
        super(view);
        this.title = view.findViewById(R.id.title_item_recommend_main);
        this.creator = view.findViewById(R.id.creator_item_recommend_main);
        this.thumbnail = view.findViewById(R.id.thumbnail_item_recommend_main);
    }
}

public class MainRecommendRecyclerAdapter
        extends RecyclerView.Adapter<MainRecommendRecyclerHolder> {

    private final MainRecommendRecyclerItem[] items;

    public MainRecommendRecyclerAdapter(MainRecommendRecyclerItem[] items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MainRecommendRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_recommend_main, viewGroup, false);

        return new MainRecommendRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecommendRecyclerHolder holder, int position) {
        MainRecommendRecyclerItem item = items[position];
        String title = item.getTitle();
        String creator = item.getCreator();
        int thumbnail = item.getThumbnail();

        holder.title.setText(title);
        holder.creator.setText(creator);
        holder.thumbnail.setImageResource(thumbnail);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
