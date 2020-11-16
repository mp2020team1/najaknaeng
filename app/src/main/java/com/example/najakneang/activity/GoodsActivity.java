package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.najakneang.R;
import com.example.najakneang.adapter.RecommendRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.YoutubeContent;

import java.util.ArrayList;

public class GoodsActivity extends AppCompatActivity {

    private final SQLiteDatabase db = MainActivity.db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        long goodsId = getIntent().getLongExtra("GOODSID",0);

        getGoodsCursor(goodsId);
        setupToolbar();
        setupGoodsView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_goods);

        String title = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupGoodsView() {
        TextView name = findViewById(R.id.name_goods);
        ImageView image = findViewById(R.id.image_goods);
        TextView quantity = findViewById(R.id.text_quantity_goods);
        TextView remain = findViewById(R.id.text_remain_goods);
        TextView type = findViewById(R.id.text_type_goods);
        // 그러고보니 메모가 있었네?.. TODO: DB에 메모 추가 후 연동 귀찮으면 버리자 -> 그래 버리자!
        EditText memo = findViewById(R.id.memo_goods);
        RecyclerView recyclerView = findViewById(R.id.recycler_recommend_goods);

        String nameStr = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        String expireDate = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
        String typeStr = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));
        name.setText(nameStr);
        image.setImageResource(DBContract.GoodsEntry.typeIconMap.get(typeStr));
        quantity.setText(cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_QUANTITY)));
        remain.setText(DBContract.GoodsEntry.getRemain(expireDate) + "일");
        type.setText(typeStr);

        ArrayList<YoutubeContent> contents = new ArrayList<>();
        Handler handler = new Handler();

        new Thread(() -> {
            MainActivity.getYoutubeContents(contents, nameStr);

            handler.post(() -> {
                RecommendRecyclerAdapter adapter = new RecommendRecyclerAdapter(contents);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                );
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            });
        }).start();
    }

    private void getGoodsCursor(long id) {
        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME,
                DBContract.GoodsEntry.COLUMN_QUANTITY,
                DBContract.GoodsEntry.COLUMN_TYPE,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE
        };

        String selection = DBContract.GoodsEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        cursor = db.query(
                DBContract.GoodsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                "1"
        );

        cursor.moveToFirst();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}