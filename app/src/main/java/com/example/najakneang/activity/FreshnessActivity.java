package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
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
    private final String[] tabSetting = {"전체", "전체"};
    static SQLiteDatabase db;
    static HashMap<String,String> storageSelection = new HashMap<String,String>(){
        {put("전체", "*"); put("냉장", "FRIDGE"); put("냉동", "FREEZE"); put("실온", "SRT");}
    };
    static HashMap<String,String> typeSelection = new HashMap<String,String>(){
        {put("전체", "*"); put("과일", "FRUIT"); put("채소", "VEGETABLE"); put("수산","FISH");
        put("육류", "MEAT"); put("유제품", "DAIRY"); put("반찬", "SIDE_DISH"); put("기타", "OTHER");}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_freshness);

        setDB();
        setupFreshnessRecycler();
        setupToolbar();
        setupTabLayout();
    }

    private void setDB(){
        try{
            Thread dbOpenThread = new Thread(() -> {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                db = dbHelper.getWritableDatabase();
            });
            dbOpenThread.start();
            dbOpenThread.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_freshness);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupTabLayout() {
        TabLayout firstTab = findViewById(R.id.tablayout_first_freshness);
        TabLayout secondTab = findViewById(R.id.tablayout_second_freshness);

        String[] firstTabNameList = {"전체", "냉장", "냉동", "실온"};
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

                //Hashmap으로 분류예정
                if (tabSetting[FIRST].equals(tabName)){}

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

    private void setupFreshnessRecycler(){
        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE,
                DBContract.GoodsEntry.COLUMN_TYPE
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
//        recycler.setLayoutManager(
//                new LinearLayoutManager(
//                        this, LinearLayoutManager.HORIZONTAL, false
//                )
//        );
    }

//    private void setupFreshnessRecycler() {
//        RecyclerView recycler = findViewById(R.id.recycler_freshness_freshness);
//
//        recycler.setAdapter(adapter);
//        recycler.setLayoutManager(new GridLayoutManager(this, 4));
//    }
//
//    // TODO: Tab을 누를때마다 바뀌는 tabSetting에 따라 items를 조절해주고 recycler 새로고침
//    private void adjustItems() {
//        // Items 조절 알고리즘 필요!
//
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}