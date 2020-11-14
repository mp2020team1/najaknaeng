package com.example.najakneang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.adapter.FridgeSectionRecyclerAdapter;
import com.example.najakneang.adapter.MainFreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.R;

public class FridgeSectionActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        String fridge = getIntent().getStringExtra("FRIDGE");
        String section = getIntent().getStringExtra("SECTION");

        setupFreshnessRecycler(fridge, section);
    }

    private void setupFreshnessRecycler(String fridge, String section) {

        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME,
                DBContract.GoodsEntry.COLUMN_QUANTITY,
                DBContract.GoodsEntry.COLUMN_IMAGE,
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE,
                DBContract.GoodsEntry.COLUMN_TYPE,
                DBContract.GoodsEntry.COLUMN_FRIDGE,
                DBContract.GoodsEntry.COLUMN_SECTION,
        };

        Cursor cursor = db.query(
                DBContract.GoodsEntry.TABLE_NAME,
                projection,
                "FRIDGE=? and SECTION=?",
                    new String[]{fridge,section},
                null,
                null,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE
        );

        RecyclerView recyclerView = findViewById(R.id.fridge_section_item);
        FridgeSectionRecyclerAdapter adapter = new FridgeSectionRecyclerAdapter(cursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    }
}
