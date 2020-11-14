package com.example.najakneang.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;

class FreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;

    public FreshnessRecyclerHolder(@NonNull View view) {
        super(view);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class FreshnessRecyclerAdapter
        extends RecyclerView.Adapter<FreshnessRecyclerHolder>{

    private final Cursor cursor;

    public FreshnessRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

    @NonNull
    @Override
    public FreshnessRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);
        return new FreshnessRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreshnessRecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
