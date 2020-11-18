package com.example.najakneang.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.FreshnessActivity;
import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.activity.SectionActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class SectionRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;
    protected final View view;
    protected final CheckBox checkbox;
    protected final FrameLayout layout;

    public SectionRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.checkbox = view.findViewById(R.id.itemSelect);
        this.layout = view.findViewById(R.id.itemFrame);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class SectionRecyclerAdapter
        extends RecyclerView.Adapter<SectionRecyclerHolder>{

    private final Cursor cursor;
    public static ArrayList<Long> removeList;

    public SectionRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
        removeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SectionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new SectionRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionRecyclerHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String name = cursor.getString(
                    cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
            long id = cursor.getLong(
                    cursor.getColumnIndex(DBContract.GoodsEntry._ID));
            String expireDate = cursor.getString(
                    cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
            long remain = DBContract.GoodsEntry.getRemain(expireDate);
            String type = cursor.getString(
                    cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));
            String state = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_STORE_STATE));

            holder.name.setText(name);
            holder.name.setTextColor(Color.parseColor(DBContract.GoodsEntry.getRemainColor(state, remain)));
            holder.remain.setText(
                    remain > 0 ? remain + "일" : remain == 0 ? "오늘까지" : Math.abs(remain) + "일 지남"
            );
            holder.image.setImageResource(DBContract.GoodsEntry.typeIconMap.get(type));
            if (SectionActivity.remove_item) {
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(view -> {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked());
                });
                holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    holder.layout.setBackgroundResource(isChecked ? R.drawable.border : R.drawable.non_border);
                    if (isChecked) removeList.add(id);
                    else removeList.remove(id);
                });
            }
            else{
                holder.checkbox.setChecked(false);
                holder.view.setOnClickListener(view -> {
                    Context context = view.getContext();
                    Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
                    intent.putExtra("GOODSID", id);
                    context.startActivity(intent);
                });
            }
        }

        if (position == getItemCount() - 1) cursor.close();
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
