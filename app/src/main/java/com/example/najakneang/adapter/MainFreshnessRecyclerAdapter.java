package com.example.najakneang.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.model.MainFreshnessRecyclerItem;
import com.example.najakneang.R;

import de.hdodenhof.circleimageview.CircleImageView;

class MainFreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;

    public MainFreshnessRecyclerHolder(@NonNull View view) {
        super(view);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class MainFreshnessRecyclerAdapter
        extends RecyclerView.Adapter<MainFreshnessRecyclerHolder> {

    private final MainFreshnessRecyclerItem[] data;

    public MainFreshnessRecyclerAdapter(MainFreshnessRecyclerItem[] data) {
        this.data = data;
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
        MainFreshnessRecyclerItem datum = data[position];
        String name = datum.getName();
        int remain = datum.getRemain();
        int image = datum.getImage();

        holder.name.setText(name);
        holder.remain.setText(Resources.getSystem().getString(R.string.format_date, remain));
        holder.image.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
