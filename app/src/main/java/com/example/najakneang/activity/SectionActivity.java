package com.example.najakneang.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.adapter.SectionRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.R;
import com.example.najakneang.model.RecyclerViewEmptySupport;

public class SectionActivity extends AppCompatActivity {

    SQLiteDatabase db = MainActivity.db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        String fridge = getIntent().getStringExtra("FRIDGE");
        String section = getIntent().getStringExtra("SECTION");
        String storeState = getIntent().getStringExtra("STORESTATE");
        setupFreshnessRecycler(fridge, section);
        setupToolbar(section, storeState);
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

        RecyclerView recyclerView = findViewById(R.id.recycler_section);
        SectionRecyclerAdapter adapter = new SectionRecyclerAdapter(cursor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar(String name, String storeState) {
        Toolbar toolbar = findViewById(R.id.toolbar_section);

        toolbar.setTitle(name);
        toolbar.setSubtitle(storeState);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.section_add:
                // item 추가 기능
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
