package com.example.najakneang.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.model.YoutubeContent;
import com.example.najakneang.R;

import java.util.ArrayList;

class MainRecommendRecyclerHolder extends RecyclerView.ViewHolder {
    protected final View view;
    protected final ImageView thumbnail;
    protected final TextView title;
    protected final TextView creator;

    public MainRecommendRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.title = view.findViewById(R.id.title_item_recommend_main);
        this.creator = view.findViewById(R.id.creator_item_recommend_main);
        this.thumbnail = view.findViewById(R.id.thumbnail_item_recommend_main);
    }
}

public class MainRecommendRecyclerAdapter
        extends RecyclerView.Adapter<MainRecommendRecyclerHolder> {

    private final ArrayList<YoutubeContent> data;

    public MainRecommendRecyclerAdapter(ArrayList<YoutubeContent> data) {
        this.data = data;
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
        YoutubeContent datum = data.get(position);
        String title = datum.getTitle();
        String creator = datum.getCreator();
        Bitmap thumbnail_bitmap = datum.getBitmap();

        holder.title.setText(title);
        holder.creator.setText(creator);
        holder.thumbnail.setImageBitmap(thumbnail_bitmap);
        holder.view.setOnClickListener(view -> {
            String videoId = datum.getVideoId();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://youtube.com/watch?v=" + videoId ));
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
