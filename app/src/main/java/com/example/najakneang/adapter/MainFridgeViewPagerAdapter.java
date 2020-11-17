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

class MainFridgeViewPagerHolder extends RecyclerView.ViewHolder {

    protected final TextView text;
    protected final FrameLayout layout;
    protected final ImageView addIcon;

    public MainFridgeViewPagerHolder(@NonNull View view) {
        super(view);
        this.text = view.findViewById(R.id.text_item_fridge_main);
        this.layout = view.findViewById(R.id.layout_item_fridge_main);
        this.addIcon = view.findViewById(R.id.add_icon_item_fridge_main);
    }
}

public class MainFridgeViewPagerAdapter
        extends RecyclerView.Adapter<MainFridgeViewPagerHolder> {

    private final Cursor cursor;

    public MainFridgeViewPagerAdapter(Cursor cursor) { this.cursor = cursor; }

    @NonNull
    @Override
    public MainFridgeViewPagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_viewpager_fridge_main, viewGroup, false);

        return new MainFridgeViewPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFridgeViewPagerHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (!cursor.moveToPosition(position)) {
            holder.layout.setBackgroundColor(context.getColor(R.color.gray_light));
            holder.addIcon.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(view -> {
                FridgeDialog fridgeDialog = new FridgeDialog(view.getContext());
                fridgeDialog.setCancelable(false);
                fridgeDialog.show();
            });

            //cursor.close();
            return;
        }

        String name = cursor.getString(
                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME));
        String category = cursor.getString(
                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_CATEGORY));

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

    @Override
    public int getItemCount() {
        return cursor.getCount() + 1;
    }
}
