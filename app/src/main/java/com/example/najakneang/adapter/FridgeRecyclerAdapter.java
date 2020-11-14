package com.example.najakneang.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;
import com.example.najakneang.activity.FridgeActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;

class FridgeRecyclerHolder extends  RecyclerView.ViewHolder{

    protected final TextView sectionName;
    protected final RecyclerView sectionPreview;

    public FridgeRecyclerHolder(@NonNull View view) {
        super(view);

        this.sectionName = view.findViewById(R.id.section_name_section_fridge);
        this.sectionPreview = view.findViewById(R.id.recycler_section_fridge);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = getAdapterPosition();
//                if(pos != recyclerView.NO_POSITION){
//                    mListener.onItemClick(v, pos);
//                }
//            }
//        });
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                int pos = getAdapterPosition();
//                if(pos != recyclerView.NO_POSITION){
//                    mLongListener.onItemLongClick(v, pos);
//                }
//                return true;
//            }
//        });
    }

}

public class FridgeRecyclerAdapter extends RecyclerView.Adapter<FridgeRecyclerHolder> {

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
        if(cursor.moveToPosition(position)) {
            String name = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_NAME));
            String fridge = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_FRIDGE));
            holder.sectionName.setText(name);
//            Context context = holder.itemView.getContext();
//            SQLiteDatabase db;
//            DBHelper dbHelper= new DBHelper(context);
//            db = dbHelper.getWritableDatabase();
//            Cursor sectionGoods = db.query(
//                    DBContract.GoodsEntry.TABLE_NAME,
//                    new String[] {
//                            DBContract.GoodsEntry.COLUMN_NAME,
//                            DBContract.GoodsEntry.COLUMN_FRIDGE,
//                            DBContract.GoodsEntry.COLUMN_SECTION,
//                            DBContract.GoodsEntry.COLUMN_EXPIREDATE
//                    },
//                    "FRIDGE=? and SECTION=?",
//                    new String[]{fridge,name},
//                    null,
//                    null,
//                    DBContract.GoodsEntry.COLUMN_EXPIREDATE
//            );
//
//            MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(sectionGoods);
//            holder.sectionPreview.setAdapter(adapter);
            //holder.sectionPreview.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
