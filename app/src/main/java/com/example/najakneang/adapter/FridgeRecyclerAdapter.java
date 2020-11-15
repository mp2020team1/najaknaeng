package com.example.najakneang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;
import com.example.najakneang.activity.FridgeActivity;
import com.example.najakneang.activity.FridgeSectionActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;


//      TODO: 구역별 미리보기 만들기
class FridgeRecyclerHolder extends  RecyclerView.ViewHolder{

    protected final View view;
    protected final TextView sectionName;
    protected final RecyclerView sectionPreview;
    private RecyclerView mainRecycler;
    private AdapterView.OnItemClickListener mCLickListener = null;
    private View.OnLongClickListener mLongClickListener = null;


    public FridgeRecyclerHolder(@NonNull View view) {
        super(view);

        this.view = view;
        this.sectionName = view.findViewById(R.id.section_name_section_fridge);
        this.sectionPreview = view.findViewById(R.id.sub_recycler_section_fridge);
        mainRecycler = view.findViewById(R.id.recycler_section_fridge);
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
            holder.view.setOnClickListener(view->{
                Context context = view.getContext();
                Intent intent = new Intent(context.getApplicationContext(), FridgeSectionActivity.class);
                intent.putExtra("SECTION", name);
                intent.putExtra("FRIDGE", fridge);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
