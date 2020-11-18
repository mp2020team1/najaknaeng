package com.example.najakneang.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;
import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.activity.SectionActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;

class FridgeRecyclerHolder extends  RecyclerView.ViewHolder{

    protected final View view;
    protected final TextView sectionName;
    protected final TextView sectionState;
    protected final RecyclerView sectionPreview;

    public FridgeRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.sectionName = view.findViewById(R.id.section_name_section_fridge);
        this.sectionState = view.findViewById(R.id.section_state_section_fridge);
        this.sectionPreview = view.findViewById(R.id.sub_recycler_section_fridge);
    }

}

public class FridgeRecyclerAdapter extends RecyclerView.Adapter<FridgeRecyclerHolder> {

    private final SQLiteDatabase db = MainActivity.db;
    private final Cursor cursor;

    public FridgeRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

    @NonNull
    @Override
    public FridgeRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_recycler_section_fridge, viewGroup, false);
        return new FridgeRecyclerHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FridgeRecyclerHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (cursor.moveToPosition(position)) {
            String name = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_NAME));
            String state = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_STORE_STATE));
            String fridge = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_FRIDGE));


            holder.sectionName.setText(name);
            holder.sectionState.setText(state);
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context.getApplicationContext(), SectionActivity.class);
                intent.putExtra("SECTION", name);
                intent.putExtra("FRIDGE", fridge);
                context.startActivity(intent);
            });
            /**
             * TODO:SectionActivity 버그가 있음!!
             */
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
                intent.putExtra("FIRDGE", fridge);
                intent.putExtra("STORAGESTATE", state);
                intent.putExtra("SECTION", name);
                context.startActivity(intent);
            });

            Cursor goodsCursor = getGoodsCursor(fridge, name);
            FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(goodsCursor);
            holder.sectionPreview.setAdapter(adapter);
            holder.sectionPreview.setLayoutManager(
                    new LinearLayoutManager(
                            context, LinearLayoutManager.HORIZONTAL, false
                    )
            );
        }

        if (position == getItemCount() - 1) cursor.close();
    }

    @Override
    public int getItemCount() { return cursor.getCount(); }

    private Cursor getGoodsCursor(String fridgeName, String sectionName) {

        String sql = "SELECT " + DBContract.GoodsEntry.TABLE_NAME+"."+ BaseColumns._ID+", "+
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_NAME +", "+
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_EXPIREDATE +", "+
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_TYPE +", "+
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_FRIDGE +", "+
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_SECTION +", "+
                DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_STORE_STATE+
                " FROM " + DBContract.GoodsEntry.TABLE_NAME +
                " INNER JOIN " + DBContract.SectionEntry.TABLE_NAME +
                " ON " + DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_SECTION +
                " = " + DBContract.SectionEntry.TABLE_NAME +"."+DBContract.SectionEntry.COLUMN_NAME + " AND " +
                DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_FRIDGE +
                " = " + DBContract.SectionEntry.TABLE_NAME +"."+DBContract.SectionEntry.COLUMN_FRIDGE +
                " WHERE " + DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_FRIDGE + " = '" + fridgeName + "' AND " +
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_SECTION + " = '" + sectionName +
                "' ORDER BY " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE + " LIMIT 3";

        return db.rawQuery(sql, null);

    }
}
