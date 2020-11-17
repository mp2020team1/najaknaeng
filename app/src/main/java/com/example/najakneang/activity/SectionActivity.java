package com.example.najakneang.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.adapter.SectionRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;

public class SectionActivity extends AppCompatActivity {

    SQLiteDatabase db = MainActivity.db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        String fridge = getIntent().getStringExtra("FRIDGE");
        String section = getIntent().getStringExtra("SECTION");
        TextView title = (TextView) findViewById(R.id.section_name);
        title.setText(section);
        setupFreshnessRecycler(fridge, section);
    }

    private void setupFreshnessRecycler(String fridge, String section) {

        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME,
                DBContract.GoodsEntry.COLUMN_QUANTITY,
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
        SectionRecyclerAdapter adapter = new SectionRecyclerAdapter(cursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    }
}
