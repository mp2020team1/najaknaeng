package com.example.najakneang.adapter;

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

class FreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;
    protected final View view;

    public FreshnessRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
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
        if (!cursor.moveToPosition(position)) return;

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

        holder.view.setOnClickListener(view -> {
            String ingredientID = cursor.getString(
                    cursor.getColumnIndex(DBContract.GoodsEntry._ID));
            Intent intent = new Intent(view.getContext(), GoodsActivity.class);
            intent.putExtra("IngredientID", ingredientID);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
