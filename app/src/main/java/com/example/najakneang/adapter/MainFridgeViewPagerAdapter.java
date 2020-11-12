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

    public MainFridgeViewPagerAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

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

                /**
                 * TODO:Dialog의 OK버튼을 누르면 테이블에 추가된 냉장고 삽입
                 * TODO:냉장고 추가 후, 커서를 한칸 뒤로 옮겨 냉장고 추가 버튼 앞에 만든 냉장고 추가
                 * TODO:dialog_fridge.xml 디자인 바꾸기
                 */

                AlertDialog fridgeDialog = new AlertDialog.Builder(context)
                        .setView(dialog_view)
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setTitle("냉장고 추가")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int pos)
                            {
                                Toast.makeText(context.getApplicationContext(),"냉장고 생성됨", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
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
                    cursor.getColumnIndex(DBContract.FridgeEntry._ID)));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount()+1;
    }
}
