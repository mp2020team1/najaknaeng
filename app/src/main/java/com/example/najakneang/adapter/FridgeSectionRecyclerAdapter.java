package com.example.najakneang.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import de.hdodenhof.circleimageview.CircleImageView;

class FridgeSectionRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;
    protected final View view;

    public FridgeSectionRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class FridgeSectionRecyclerAdapter
        extends RecyclerView.Adapter<FridgeSectionRecyclerHolder>{

    private final Cursor cursor;

    public FridgeSectionRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

    @NonNull
    @Override
    public FridgeSectionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new FridgeSectionRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeSectionRecyclerHolder holder, int position) {
        if (!cursor.moveToPosition(position) || position>2) return;

        String name = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        long id = cursor.getLong(
                cursor.getColumnIndex(DBContract.GoodsEntry._ID));
        String expireDate = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
        long remain = DBContract.GoodsEntry.getRemain(expireDate);
        String type = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));

        holder.name.setText(name);
        holder.remain.setText(
                remain > 0 ? remain + "일" : remain == 0 ? "오늘까지" : Math.abs(remain) + "일 지남"
        );
        holder.image.setImageResource(DBContract.GoodsEntry.typeIconMap.get(type));
        holder.view.setOnClickListener(view->{
            Context context = view.getContext();
            Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
            intent.putExtra("GOODSID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

