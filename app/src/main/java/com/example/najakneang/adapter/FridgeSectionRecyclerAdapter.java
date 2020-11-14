package com.example.najakneang.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import de.hdodenhof.circleimageview.CircleImageView;

class FridgeSectionRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;

    public FridgeSectionRecyclerHolder(@NonNull View view) {
        super(view);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
    }
}

public class FridgeSectionRecyclerAdapter
        extends RecyclerView.Adapter<MainFreshnessRecyclerHolder>{

    private final Cursor cursor;

    public FridgeSectionRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

    @NonNull
    @Override
    public MainFreshnessRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new MainFreshnessRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFreshnessRecyclerHolder holder, int position) {
        if (!cursor.moveToPosition(position) || position>2) return;

        String name = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        String expireDate = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
        long remain = DBContract.GoodsEntry.getRemain(expireDate);
        String type = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));

        int image;
        switch(type){
            case "fruit":
                image = R.drawable.fruit;
                break;
            case "vegetable":
                image = R.drawable.vegetable;
                break;
            case "meat":
                image = R.drawable.meat;
                break;
            case "fish":
                image = R.drawable.fish;
                break;
            default:
                image = R.drawable.ic_launcher_background;
        }

        holder.name.setText(name);
        if(remain>0){holder.remain.setText(remain + "일");}
        else if(remain==0){holder.remain.setText("오늘까지");}
        else{holder.remain.setText(Math.abs(remain) + "일 지남");}

        holder.image.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

