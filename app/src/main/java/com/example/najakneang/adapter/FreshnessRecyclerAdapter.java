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

import com.example.najakneang.activity.FreshnessActivity;
import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import java.util.ArrayList;
// 파일 설명 : 신선도 RecyclerView 와 보여줄 데이터를 연결하는 Adapter class
// 파일 주요 기능 : RecyclerView의 각 홀더에 데이터 및 이벤트 연결

// 클래스 설명 : RecyclerView의 각 View를 저장하는 Holder 객체 class
class FreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final ImageView image;
    protected final ImageView moreIcon;
    protected final CheckBox checkbox;
    protected final FrameLayout layout;

    // 메서드 설명 : Holder 객체를 생성하고 item_recycler_freshness_main 레이아웃의 View와 연결
    public FreshnessRecyclerHolder(@NonNull View view) {
        super(view);
        this.name = view.findViewById(R.id.name_item_freshness_main);
        this.remain = view.findViewById(R.id.remain_item_freshness_main);
        this.image = view.findViewById(R.id.image_item_freshness_main);
        this.moreIcon = view.findViewById(R.id.more_icon_item_freshness_main);
        this.checkbox = view.findViewById(R.id.itemSelect);
        this.layout = view.findViewById(R.id.itemFrame);
    }
}

// 클래스 설명 : RecyclerView와 데이터를 연결하는 Adapter class
public class FreshnessRecyclerAdapter
        extends RecyclerView.Adapter<FreshnessRecyclerHolder>{

    private final Cursor cursor;
    public static ArrayList<Long> removeList;

    // 메서드 설명 : 입력받은 cursor를 Adapter의 cursor에 복사
    public FreshnessRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
        removeList = new ArrayList<>();
    }

    // 메서드 설명 : Holder 객체를 item_recycler_freshness_main 레이아웃에 따라 생성
    @NonNull
    @Override
    public FreshnessRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new FreshnessRecyclerHolder(view);
    }

    // 메서드 설명 : 생성된 Holder 객체와 cursor의 데이터를 연결
    @Override
    public void onBindViewHolder(@NonNull FreshnessRecyclerHolder holder, int position) {
        // cursor가 position 위치로 움직일 수 없으면 moreIcon을 추가
        if (!cursor.moveToPosition(position)) {
            if (holder.itemView.getContext().getClass() == MainActivity.class && cursor.getCount() != 0) {
                holder.moreIcon.setVisibility(View.VISIBLE);
            }
            cursor.close();
            return;
        }
        // cursor의 데이터 읽기
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

        // Holder와 데이터를 연결
        holder.name.setText(name);
        holder.name.setSelected(true);
        holder.name.setTextColor(Color.parseColor(DBContract.GoodsEntry.getRemainColor(state, remain)));
        holder.remain.setText(
                remain > 0 ? remain + "일" : remain == 0 ? "오늘까지" : Math.abs(remain) + "일 지남"
        );
        holder.remain.setSelected(true);
        Integer image = DBContract.GoodsEntry.typeIconMap.get(type);
        holder.image.setImageResource(image);

        // TODO: FreshnessActivity checkbox
        if (FreshnessActivity.remove_item) {
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
            holder.itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
                intent.putExtra("GOODSID", id);
                context.startActivity(intent);
            });
        }
    }

    //메서드 설명 : RecyclerView의 크기를 반환
    @Override
    public int getItemCount() {
        return cursor.getCount() + 1;
    }
}
