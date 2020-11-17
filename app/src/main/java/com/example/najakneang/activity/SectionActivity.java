package com.example.najakneang.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.adapter.SectionRecyclerAdapter;
import com.example.najakneang.db.DBContract;
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

        String sql =
                "SELECT " + DBContract.GoodsEntry.TABLE_NAME + "." + BaseColumns._ID + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_NAME + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_QUANTITY + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_REGISTDATE + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_TYPE + ", " +
                        DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE + ", " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_SECTION + ", " +
                        DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_STORE_STATE +
                        " FROM " + DBContract.GoodsEntry.TABLE_NAME +
                        " INNER JOIN " + DBContract.SectionEntry.TABLE_NAME +
                        " ON " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_SECTION + " = " +
                        DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " AND " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                        DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                        " WHERE " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = '" + fridge + "' AND " +
                        DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_SECTION + " = '" + section +
                        "' ORDER BY " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE;

        Cursor cursor = db.rawQuery(sql, null);

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
