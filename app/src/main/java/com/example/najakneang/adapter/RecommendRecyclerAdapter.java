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

// TODO: 유튜브 추천 어댑터
class RecommendRecyclerHolder extends RecyclerView.ViewHolder {
    protected final View view;
    protected final ImageView thumbnail;
    protected final TextView title;
    protected final TextView creator;

    public RecommendRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.title = view.findViewById(R.id.title_item_recommend);
        this.creator = view.findViewById(R.id.creator_item_recommend);
        this.thumbnail = view.findViewById(R.id.thumbnail_item_recommend);
    }
}

public class RecommendRecyclerAdapter
        extends RecyclerView.Adapter<RecommendRecyclerHolder> {

    private final ArrayList<YoutubeContent> data;

    public RecommendRecyclerAdapter(ArrayList<YoutubeContent> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecommendRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_recommend, viewGroup, false);

        return new RecommendRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendRecyclerHolder holder, int position) {
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
