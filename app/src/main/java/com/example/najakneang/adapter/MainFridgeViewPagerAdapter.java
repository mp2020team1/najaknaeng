package com.example.najakneang.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;
import com.example.najakneang.activity.FridgeActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.FridgeDialog;

// 파일 설명 : 메인화면의 냉장고 ViewPager와 보여줄 데이터를 연결하는 Adapter class
// 파일 주요 기능 : RecyclerView의 각 홀더에 데이터 및 이벤트 연결

// 클래스 설명 : RecyclerView의 각 View를 저장하는 Holder 객체 class
class MainFridgeViewPagerHolder extends RecyclerView.ViewHolder {

    protected final TextView text;
    protected final FrameLayout layout;
    protected final ImageView addIcon;

    // 메서드 설명 : Holder 객체를 생성하고 item_viewpager_fridge_main 레이아웃의 View와 연결
    public MainFridgeViewPagerHolder(@NonNull View view) {
        super(view);
        this.text = view.findViewById(R.id.text_item_fridge_main);
        this.layout = view.findViewById(R.id.layout_item_fridge_main);
        this.addIcon = view.findViewById(R.id.add_icon_item_fridge_main);
    }
}

// 클래스 설명 : RecyclerView와 데이터를 연결하는 Adapter class
public class MainFridgeViewPagerAdapter
        extends RecyclerView.Adapter<MainFridgeViewPagerHolder> {

    private final Cursor cursor;

    // 메서드 설명 : 입력받은 cursor를 Adapter의 cursor에 복사
    public MainFridgeViewPagerAdapter(Cursor cursor) { this.cursor = cursor; }

    // 메서드 설명 : Holder 객체를 item_viewpager_fridge_main 레이아웃에 따라 생성
    @NonNull
    @Override
    public MainFridgeViewPagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_viewpager_fridge_main, viewGroup, false);

        return new MainFridgeViewPagerHolder(view);
    }

    // 메서드 설명 : 생성된 Holder 객체와 cursor의 데이터를 연결
    @Override
    public void onBindViewHolder(@NonNull MainFridgeViewPagerHolder holder, int position) {
        Context context = holder.itemView.getContext();
        // cursor를 position의 위치 옮길수 없으면 냉장고 추가아이콘 추가
        if (!cursor.moveToPosition(position)) {
            holder.layout.setBackgroundColor(context.getColor(R.color.gray_light));
            holder.addIcon.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(view -> {
                MainActivity.fridge_id = position; // 클릭시 생성한 냉장고를 ViewPager에서 보이도록 함
                FridgeDialog fridgeDialog = new FridgeDialog(view.getContext());
                fridgeDialog.setCancelable(false);
                fridgeDialog.show();
            });
            return;
        }
        // cursor를 position의 위치 옮길수 있으면 데이터값을 읽기
        String name = cursor.getString(
                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME));
        String category = cursor.getString(
                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_CATEGORY));

        // Holder와 데이터를 연결
        holder.text.setText(name);
        holder.layout.setBackgroundResource(DBContract.FridgeEntry.categoryImageMap.get(category));
        holder.itemView.setOnClickListener(view -> {
            MainActivity.fridge_id = holder.getAdapterPosition();
            Intent intent = new Intent(context.getApplicationContext(), FridgeActivity.class);
            intent.putExtra("FRIDGE", name);
            intent.putExtra("CATEGORY", category);
            context.startActivity(intent);
        });
    }

    // 메서드 설명 : RecyclerView의 크기를 반환
    @Override
    public int getItemCount() {
        return cursor.getCount() + 1;
    }
}
