package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.RecyclerViewEmptySupport;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.tabs.TabLayout;

public class FreshnessActivity extends AppCompatActivity {

    private static final int FIRST = 0;
    private static final int SECOND = 1;

    private static String select_first = DBContract.SectionEntry.COLUMN_STORE_STATE;
    private static String select_second = DBContract.GoodsEntry.COLUMN_TYPE;

    private final SQLiteDatabase db = MainActivity.db;

    private final String[] tabSetting = {"전체", "전체"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_freshness);

        setupFreshnessRecycler();
        setupToolbar();
        setupTabLayout();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_freshness);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTabLayout() {
        TabLayout firstTab = findViewById(R.id.tablayout_first_freshness);
        TabLayout secondTab = findViewById(R.id.tablayout_second_freshness);

        for (String name: getResources().getStringArray(R.array.firstTab) ) {
            firstTab.addTab(firstTab.newTab().setText(name));
        }

        for (String name: getResources().getStringArray(R.array.secondTab)) {
            secondTab.addTab(secondTab.newTab().setText(name));
        }

        firstTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();

                select_first = tabName.equals("전체") ? DBContract.SectionEntry.COLUMN_STORE_STATE : "\"" + tabName + "\"";
                setupFreshnessRecycler();

                tabSetting[FIRST] = tabName;
                tabSetting[SECOND] = "전체";
                TabLayout.Tab secondEveryTab = secondTab.getTabAt(0);
                secondEveryTab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        secondTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();

                select_second = tabName.equals("전체") ? DBContract.GoodsEntry.COLUMN_TYPE : "\"" + tabName + "\"";
                setupFreshnessRecycler();
                if (tabSetting[SECOND].equals(tabName)) return;

                tabSetting[SECOND] = tabName;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_freshness, menu);
        return true;
    }

    private void setupFreshnessRecycler() {
        // TODO: 실온은 따로 팬트리만 불러와야함.. 우짠다냐

        String sql =
                "SELECT " + DBContract.GoodsEntry.TABLE_NAME + "." + BaseColumns._ID + ", " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_NAME + ", " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE + ", " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_TYPE + ", " +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE + ", " +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_STORE_STATE +
                " FROM " + DBContract.GoodsEntry.TABLE_NAME +
                " INNER JOIN " + DBContract.SectionEntry.TABLE_NAME +
                " INNER JOIN " + DBContract.FridgeEntry.TABLE_NAME +
                " ON " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME + " AND " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_SECTION + " = " +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " AND " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                " WHERE " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_STORE_STATE + " = " + select_first + " AND " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_TYPE + " = " + select_second +
                " ORDER BY " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE;

        Cursor cursor = db.rawQuery(sql, null);

        /**
         * TODO: Recyclerview의 margin을 조정하는등을 해서 맞출 필요가 있음
         * 참고 : https://github.com/google/flexbox-layout
         */

        TextView emptyView = findViewById(R.id.empty_view_recycler_freshness);
        RecyclerViewEmptySupport recycler = findViewById(R.id.recycler_freshness_freshness);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(cursor);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        recycler.setLayoutManager(layoutManager);
        recycler.setEmptyView(emptyView);
        recycler.setAdapter(adapter);
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