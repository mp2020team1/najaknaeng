package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.najakneang.adapter.MainFreshnessRecyclerAdapter;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.model.MainFreshnessRecyclerItem;
import com.example.najakneang.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class FreshnessActivity extends AppCompatActivity {

    SQLiteDatabase DB;
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private final String[] tabSetting = {"전체", "전체"};
    private final MainFreshnessRecyclerItem[] items = {
            new MainFreshnessRecyclerItem(
                    "품목 1", R.drawable.ic_launcher_background, 3),
            new MainFreshnessRecyclerItem(
                    "품목 2", R.drawable.ic_launcher_background, 30)
    };
    private final MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(items);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB = setupDateBase();
        initTable();
        loadData();

        setContentView(R.layout.activity_freshness);

        setupToolbar();

        setupTabLayout();
        setupFreshnessRecycler();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_freshness);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTabLayout() {
        TabLayout firstTab = findViewById(R.id.tablayout_first_freshness);
        TabLayout secondTab = findViewById(R.id.tablayout_second_freshness);

        String[] firstTabNameList = {"전체", "냉장", "냉동", "실온", "기타"};
        String[] secondTabNameList = {"전체", "과일", "채소", "수산", "육류", "유제품", "반찬", "기타"};

        for (String name: firstTabNameList) {
            firstTab.addTab(firstTab.newTab().setText(name));
        }

        for (String name: secondTabNameList) {
            secondTab.addTab(secondTab.newTab().setText(name));
        }

        firstTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();
                if (tabSetting[FIRST].equals(tabName)) return;

                tabSetting[FIRST] = tabName;
                tabSetting[SECOND] = "전체";
                TabLayout.Tab secondEveryTab = secondTab.getTabAt(0);
                secondEveryTab.select();

                adjustItems();
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
                if (tabSetting[SECOND].equals(tabName)) return;

                tabSetting[SECOND] = tabName;

                adjustItems();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupFreshnessRecycler() {
        RecyclerView recycler = findViewById(R.id.recycler_freshness_freshness);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this, 4));
    }

    // TODO: Tab을 누를때마다 바뀌는 tabSetting에 따라 items를 조절해주고 recycler 새로고침
    private void adjustItems() {
        // Items 조절 알고리즘 필요!

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        if(DB != null){
            String sqlQuery = "SELECT * FROM Items";
            Cursor cursor = null;

            cursor = DB.rawQuery(sqlQuery, null);

            if (cursor.moveToNext()){
                String name = cursor.getString(0);
                int quantity = cursor.getInt(1);
                String date = cursor.getString(2);
                String classID = cursor.getString(3);
                String fridgeID = cursor.getString(4);
                String storageID = cursor.getString(5);
            }
        }
    }

    private SQLiteDatabase setupDateBase() {
        SQLiteDatabase db = null;

        File file = new File(getFilesDir(), DBHelper.DB_NAME);
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        }catch (SQLException se){
            se.printStackTrace();
        }

        if(db == null){
            Log.e("e","error");
        }

        return db;
    }

    private void initTable(){
        if(DB == null){
            String sqlcreate = "CREATE TABLE IF NOT EXISTS Items (" +
                    "NAME "         + "TEXT," +
                    "QUANTITY "     + "INTEGER NOT NULL," +
                    "REGISTDATE"    + "TEXT," +
                    "EXPIREDATE "   + "TEXT," +
                    "TYPE "         + "TEXT," +
                    "FRIDGE "       + "TEXT," +
                    "STORESTATE"    + "TEXT," +
                    "SECTION "      + "TEXT" + ")";
            DB.execSQL(sqlcreate);
        }
    }

}