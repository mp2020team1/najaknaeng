package com.example.najakneang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.R;
import com.example.najakneang.activity.FridgeActivity;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.Dialog_Fridge;

class MainFridgeViewPagerHolder extends RecyclerView.ViewHolder {
    protected final View view;
    protected final TextView text;

    public MainFridgeViewPagerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.text = view.findViewById(R.id.text_item_fridge_main);
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
        if (!cursor.moveToPosition(position)){
            holder.text.setText("냉장고 추가");
            holder.view.setOnClickListener(view -> {
                final Context context = view.getContext();
                final View dialog_view= view.inflate(context,R.layout.dialog_fridge,null);

                Dialog_Fridge dialog_fridge = new Dialog_Fridge(context);
                dialog_fridge.setCancelable(false);
                dialog_fridge.show();
            });
            return;
        }

        String name = cursor.getString(
                cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME));
        holder.text.setText(name);

        holder.view.setOnClickListener(view -> {
            Context context = view.getContext();
            Log.i("cursor", cursor.getPosition() + "입니다.");

            Intent intent = new Intent(context.getApplicationContext(), FridgeActivity.class);
            intent.putExtra("FRIDGE", cursor.getString(
                    cursor.getColumnIndex(DBContract.FridgeEntry.COLUMN_NAME)));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount()+1;
    }
}
