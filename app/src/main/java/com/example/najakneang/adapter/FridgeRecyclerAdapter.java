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
import com.example.najakneang.activity.SectionActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.RecyclerViewEmptySupport;

// 파일 설명 : FridgeActivity의 RecyclerView와 보여줄 데이터를 연결하는 Adapter class
// 파일 주요 기능 : RecyclerView의 각 홀더에 데이터 및 이벤트 연결

// 클래스 설명 : RecyclerView의 각 View를 저장하는 Holder 객체 class
class FridgeRecyclerHolder extends  RecyclerView.ViewHolder{

    protected final View view;
    protected final TextView sectionName;
    protected final TextView sectionState;
    protected final RecyclerViewEmptySupport sectionPreview;

    // 메서드 설명 : Holder 객체를 생성하고 item_recycler_section_fridge 레이아웃의 View와 연결
    public FridgeRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.sectionName = view.findViewById(R.id.section_name_section_fridge);
        this.sectionState = view.findViewById(R.id.section_state_section_fridge);
        this.sectionPreview = view.findViewById(R.id.sub_recycler_section_fridge);
    }

}

// 클래스 설명 : RecyclerView와 데이터를 연결하는 Adapter class
public class FridgeRecyclerAdapter extends RecyclerView.Adapter<FridgeRecyclerHolder> {

    private final SQLiteDatabase db = MainActivity.db;
    private final Cursor cursor;

    // 메서드 설명 : 입력 받은 cursor를 Adapter의 cursor에 복사
   public FridgeRecyclerAdapter(Cursor cursor) { this.cursor = cursor; }

   // 메서드 설명 : Holder 객체를 item_recycler_section_fridge 레이아웃에 따라 생성
    @NonNull
    @Override
    public FridgeRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_recycler_section_fridge, viewGroup, false);
        return new FridgeRecyclerHolder(view);

    }

    // 메서드 설명 : 생성된 Holder 객체와 cursor의 데이터를 연결
    @Override
    public void onBindViewHolder(@NonNull FridgeRecyclerHolder holder, int position) {
        Context context = holder.itemView.getContext();
        View view = holder.itemView;

        // cursor를 position 위치로 움직일수 있으면
        if (cursor.moveToPosition(position)) {

            // cursor의 데이터 읽기
            String name = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_NAME));
            String state = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_STORE_STATE));
            String fridge = cursor.getString(
                    cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_FRIDGE));
            String category = cursor.getString(
                    cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_CATEGORY));

            // Holder와 데이터를 연결
           holder.sectionName.setText(name);
            holder.sectionState.setText(state);
            view.setOnClickListener(v -> {
                Intent intent = new Intent(context.getApplicationContext(), SectionActivity.class);
                intent.putExtra("SECTION", name);
                intent.putExtra("FRIDGE", fridge);
                intent.putExtra("STORESTATE", state);
                intent.putExtra("CATEGORY", category);
                context.startActivity(intent);
            });

            // 현재 냉장고에 있는 구역들 속 품목에 대한 데이터를 goodCursor에 복사
            Cursor goodsCursor = getGoodsCursor(fridge, name);

            // 각 구역의 품목 cursor를 신선도 RecyclerAdapter로 적용
            TextView emptyView = view.findViewById(R.id.empty_view_sub_recycler_section_fridge);
            FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(goodsCursor);
            holder.sectionPreview.setExpend(true);
            holder.sectionPreview.setEmptyView(emptyView);
            holder.sectionPreview.setAdapter(adapter);
            holder.sectionPreview.setLayoutManager(
                    new LinearLayoutManager(
                            context, LinearLayoutManager.HORIZONTAL, false
                    )
            );
        }

        if (position == getItemCount() - 1) cursor.close();
    }

    // 메서드 설명 : RecyclerView의 크기를 반환
    @Override
    public int getItemCount() { return cursor.getCount(); }

    // 메서드 설명 : 품목의 이름, 수량, 유통기한, 저장상태등의 데이터값을 cursor로 반환
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
