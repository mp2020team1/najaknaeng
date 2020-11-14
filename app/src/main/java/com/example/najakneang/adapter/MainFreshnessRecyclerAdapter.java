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
        extends RecyclerView.Adapter<MainFreshnessRecyclerHolder>{

    private final Cursor cursor;

    public MainFreshnessRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

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
        if(type.equals("fruit")){ image = R.drawable.fruit; }
        else if(type.equals("vegetable")){ image = R.drawable.vegetable; }
        else if(type.equals("meat")){ image = R.drawable.meat; }
        else if(type.equals("fish")){ image = R.drawable.fish; }
        else{ image = R.drawable.ic_launcher_background; }

        holder.name.setText(name);
        if(remain>0){holder.remain.setText(remain + "일");}
        else if(remain==0){holder.remain.setText("오늘까지");}
        else{holder.remain.setText(Math.abs(remain) + "일 지남");}

        holder.image.setImageResource(image);
//        int image = cursor.getInt(
//                cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_IMAGE));
        /**
         * type이름으로 바로 리소스 받기 제작 실패
         */
//        int resID =  R.drawable.class.getField(type).getInt("");
//        int resID = context.getResources().getIdentifier(
//                type,"drawable", context.getPackageName());
//        int image = resID;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
