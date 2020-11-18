package com.example.najakneang.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.FreshnessActivity;
import com.example.najakneang.activity.GoodsActivity;
import com.example.najakneang.activity.MainActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class FreshnessRecyclerHolder extends RecyclerView.ViewHolder {
    protected final TextView name;
    protected final TextView remain;
    protected final CircleImageView image;
    protected final ImageView moreIcon;
    protected final CheckBox checkbox;
    protected final FrameLayout layout;

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

public class FreshnessRecyclerAdapter
        extends RecyclerView.Adapter<FreshnessRecyclerHolder>{

    private final Cursor cursor;
    public static ArrayList<Long> removeList;

    public FreshnessRecyclerAdapter(Cursor cursor) {
        this.cursor = cursor;
        if(FreshnessActivity.remove_item){ removeList = new ArrayList<>();}
    }

    @NonNull
    @Override
    public FreshnessRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_freshness_main, viewGroup, false);

        return new FreshnessRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreshnessRecyclerHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            if (holder.itemView.getContext().getClass() == MainActivity.class && cursor.getCount() != 0) {
                holder.moreIcon.setVisibility(View.VISIBLE);
            }
            cursor.close();
            return;
        }

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

        holder.name.setText(name);
        holder.name.setSelected(true);
        holder.name.setTextColor(Color.parseColor(DBContract.GoodsEntry.getRemainColor(state, remain)));
        holder.remain.setText(
                remain > 0 ? remain + "일" : remain == 0 ? "오늘까지" : Math.abs(remain) + "일 지남"
        );
        Integer image = DBContract.GoodsEntry.typeIconMap.get(type);
        holder.image.setImageResource(image);

        if(FreshnessActivity.remove_item){
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(view -> {
                holder.checkbox.setChecked(holder.checkbox.isChecked()?false:true);
            });
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   holder.layout.setBackgroundResource(isChecked?R.drawable.border:R.drawable.non_border);
                   if(isChecked){removeList.add(id);}
                   else{removeList.remove(id);}
               }
            });
        }
        else{
            holder.checkbox.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context.getApplicationContext(), GoodsActivity.class);
                intent.putExtra("GOODSID", id);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount() + 1;
    }
}
