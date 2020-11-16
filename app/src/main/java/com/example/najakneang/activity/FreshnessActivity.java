package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FreshnessRecyclerAdapter;
import com.example.najakneang.adapter.MainFridgeViewPagerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

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

                select_first = tabName.equals("전체") ? DBContract.SectionEntry.COLUMN_STORE_STATE : tabName;
                setupFreshnessRecycler();
                //Hashmap으로 분류예정
                if (tabSetting[FIRST].equals(tabName)){ }

                tabSetting[FIRST] = tabName;
                tabSetting[SECOND] = "전체";
                TabLayout.Tab secondEveryTab = secondTab.getTabAt(0);
                secondEveryTab.select();

//                adjustItems();
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

                select_second = tabName.equals("전체") ? DBContract.GoodsEntry.COLUMN_TYPE : tabName;
                setupFreshnessRecycler();
                if (tabSetting[SECOND].equals(tabName)) return;

                tabSetting[SECOND] = tabName;

//                adjustItems();
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
        /**
         * TODO:SQL 구문에 문제가 있음! 확인바람!
         */
        String sql = "SELECT " + DBContract.GoodsEntry.TABLE_NAME + "." + BaseColumns._ID + "," +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_NAME + "," +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE + "," +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_TYPE + "," +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE + "," +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_STORE_STATE +
                " FROM " + DBContract.SectionEntry.TABLE_NAME + " INNER JOIN " + DBContract.GoodsEntry.TABLE_NAME +
                " ON " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                " WHERE " + DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_STORE_STATE + " = " + select_first + " AND " +
                DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_TYPE + "=" + select_second +
                " ORDER BY " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_EXPIREDATE;

        Cursor cursor = db.rawQuery(sql, null);

        /**
         * TODO: Recyclerview의 margin을 조정하는등을 해서 맞출 필요가 있음
         * 참고 : https://github.com/google/flexbox-layout
         */

        RecyclerView recycler = findViewById(R.id.recycler_freshness_freshness);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        recycler.setLayoutManager(layoutManager);

        FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(cursor);
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