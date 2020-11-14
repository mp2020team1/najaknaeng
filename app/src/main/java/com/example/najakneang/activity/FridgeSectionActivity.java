package com.example.najakneang.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.adapter.MainFreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.R;

public class FridgeSectionActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    String[] projection = {
            BaseColumns._ID,
            DBContract.GoodsEntry.COLUMN_NAME,
            DBContract.GoodsEntry.COLUMN_EXPIREDATE,
            DBContract.GoodsEntry.COLUMN_IMAGE
    };

    Cursor cursor = db.query(
            DBContract.GoodsEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            DBContract.GoodsEntry.COLUMN_EXPIREDATE
    );

    private final MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(cursor);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        setupFreshnessRecycler();
    }
    private void setupFreshnessRecycler() {
        RecyclerView recyclerView = findViewById(R.id.fridge_section_item);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    }
}
