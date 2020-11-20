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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.activity.SectionActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import java.util.ArrayList;

// 파일 설명 : SectionActivity의 RecyclerView와 보여줄 데이터를 연결하는 Adapter class
// 파일 주요 기능 : RecyclerView의 각 홀더에 데이터 및 이벤트 연결

// 클래스 설명 : SectionActivity의 RecyclerView의 각 View를 저장하는 Holder 객체 class
class SectionRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final ImageView image;
    protected final View view;
    protected final CheckBox checkbox;
    protected final FrameLayout layout;

    // 메서드 설명 : Holder 객체를 생성하고 item_recycler_freshness_main 레이아웃의 View와 연결
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

// 클래스 설명 : SectionActivity의 RecyclerView와 데이터를 연결하는 Adapter class
public class SectionRecyclerAdapter
        extends RecyclerView.Adapter<SectionRecyclerHolder>{

    private final Cursor cursor;
    public static ArrayList<Long> removeList;

    // 메서드 설명 : 입력받은 cursor를 RecyclerView의 cursor에 복사
    public SectionRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
        removeList = new ArrayList<>();
    }

    // 메서드 설명 : Holder 객체를 item_recycler_freshness_main 레이아웃에 따라 생성
    @NonNull
    @Override
    public SectionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new SectionRecyclerHolder(view);
    }

    // 메서드 설명 : 생성된 Holder 객체와 cursor의 데이터를 연결
    @Override
    public void onBindViewHolder(@NonNull SectionRecyclerHolder holder, int position) {
        // cursor가 position의 위치로 이동이 가능하면
        if (cursor.moveToPosition(position)) {
            // cursor의 데이터값을 복사
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
            // Holder에 데이터 값을 연결
            holder.name.setText(name);
            holder.name.setTextColor(Color.parseColor(DBContract.GoodsEntry.getRemainColor(state, remain)));
            holder.remain.setText(
                    remain > 0 ? remain + "일" : remain == 0 ? "오늘까지" : Math.abs(remain) + "일 지남"
            );
            holder.image.setImageResource(DBContract.GoodsEntry.typeIconMap.get(type));
            // remove_item이 true이면 체크박스를 활성화
            if (SectionActivity.remove_item) {
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(view -> {
                    holder.checkbox.setChecked(!holder.checkbox.isChecked());
                });
                // Holder 체크박스에 체크되면 removeList에 추가
                holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    holder.layout.setBackgroundResource(isChecked ? R.drawable.border : R.drawable.non_border);
                    if (isChecked) removeList.add(id);
                    else removeList.remove(id);
                });
            }
            // remove_item이 false이면 체크박스를 비활성화
            else{
                holder.checkbox.setChecked(false);
                // Holder 클릭시 해당 품목의 GoodsActivity로 이동
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

    // 메서드 설명 : RecyclerView의 크기를 반환
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
