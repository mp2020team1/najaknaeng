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

import com.example.najakneang.activity.FridgeSectionActivity;
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
//        int quantity = cursor.getInt(
//                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_QUANTITY));
//        String registDate = cursor.getString(
//                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_REGISTDATE));
        long id = cursor.getLong(
                cursor.getColumnIndex(DBContract.GoodsEntry._ID));
        String expireDate = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
        long remain = DBContract.GoodsEntry.getRemain(expireDate);
        String type = cursor.getString(
                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));
//        String fridge = cursor.getString(
//                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_FRIDGE));
//        String section = cursor.getString(
//                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_SECTION));
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
        holder.view.setOnClickListener(view->{
            Context context = view.getContext();
            Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
            intent.putExtra("GOODSID", id);
//            intent.putExtra("NAME", name);
//            intent.putExtra("QUANTITY", quantity);
//            intent.putExtra("REGISTDATE", registDate);
//            intent.putExtra("EXPIREDATE", expireDate);
//            intent.putExtra("TYPE", type);
//            intent.putExtra("FRIDGE", fridge);
//            intent.putExtra("SECTION", section);
//            intent.putExtra("REMAIN", remain);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

