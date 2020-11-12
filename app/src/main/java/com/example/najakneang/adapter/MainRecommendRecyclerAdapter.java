package com.example.najakneang.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.model.MainRecommendRecyclerItem;
import com.example.najakneang.R;

import java.util.ArrayList;

public class MainRecommendRecyclerAdapter
        extends RecyclerView.Adapter<MainRecommendRecyclerAdapter.MainRecommendRecyclerHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private final ArrayList<MainRecommendRecyclerItem> items;
    private OnItemClickListener listener = null;

    public MainRecommendRecyclerAdapter(ArrayList<MainRecommendRecyclerItem> items) {
        this.items = items;
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    class MainRecommendRecyclerHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected TextView title;
        protected TextView creator;

        public MainRecommendRecyclerHolder(@NonNull View view) {
            super(view);
            this.title = view.findViewById(R.id.title_item_recommend_main);
            this.creator = view.findViewById(R.id.creator_item_recommend_main);
            this.thumbnail = view.findViewById(R.id.thumbnail_item_recommend_main);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    if(listener != null){
                        listener.onItemClick(v, position);
                    }
                }
            });
        }

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
        MainRecommendRecyclerItem item = items.get(position);
        String title = item.getTitle();
        String creator = item.getCreator();
        Bitmap thumbnail_bitmap = item.getBitmap();

        holder.title.setText(title);
        holder.creator.setText(creator);
        holder.thumbnail.setImageBitmap(thumbnail_bitmap);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
