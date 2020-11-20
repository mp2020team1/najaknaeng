package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.GoodsDialog;
import com.example.najakneang.model.RecyclerViewEmptySupport;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.tabs.TabLayout;

// 파일 설명 : 전체 재료를 보여주는 엑티비티
// 파일 주요 기능 : Toolbar에는 뒤로가기 버튼, 재료 추가 및 제거 기능이 있는 버튼
//                 Tab을 통하여 보관상태, 재료 종류를 분류하여 볼 수 있음
//                 RecyclerView에 존재하는 재료 모두 표시

public class FreshnessActivity extends AppCompatActivity {

    private static final int FIRST = 0;
    private static final int SECOND = 1;

    private Toolbar toolbar;

    // 현재 선택한 탭의 내용이 무엇인지 저장해놓는 String
    private static String select_first = DBContract.SectionEntry.COLUMN_STORE_STATE;
    private static String select_second = DBContract.GoodsEntry.COLUMN_TYPE;

    private final SQLiteDatabase db = MainActivity.db;

    private final String[] tabSetting = {"전체", "전체"};

    // 현재 상태가 아이템 제거 상태인가 추가 상태인가 판단
    public static boolean remove_item = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_freshness);

        setupFreshnessRecycler();
        setupToolbar();
        setupTabLayout();
    }

    // 매서드 설명 : Activity이동시에 정보 갱신
    @Override
    protected void onResume(){
        super.onResume();

        setupFreshnessRecycler();
    }

    // 매서드 설명 : toolbar 기본설정
    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar_freshness);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 매서드 설명 : 탭의 기본 설정을 해주고 클릭됐을때, 그에 따른 분류 조건 설정
    private void setupTabLayout() {
        TabLayout firstTab = findViewById(R.id.tablayout_first_freshness);
        TabLayout secondTab = findViewById(R.id.tablayout_second_freshness);

        // 보관상태 탭의 기본 설정
        for (String name: getResources().getStringArray(R.array.firstTab) ) {
            firstTab.addTab(firstTab.newTab().setText(name));
        }

        // 종류 탭의 기본 설정
        for (String name: getResources().getStringArray(R.array.secondTab)) {
            secondTab.addTab(secondTab.newTab().setText(name));
        }

        // 보관상태 탭이 클릭됐을 경우
        firstTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();

                // 보관상태를 분류 조건을 바꿔주고 갱신
                select_first = tabName.equals("전체") ? DBContract.SectionEntry.COLUMN_STORE_STATE : "\"" + tabName + "\"";
                setupFreshnessRecycler();

                // 종류 탭은 "전체"로 바꿔줌
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

        // 종류 탭이 클릭됐을 경우
        secondTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabName = tab.getText().toString();

                // 종류의 조건을 바꿔주고 갱신
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

    // 매서드 설명 : Toolbar의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_freshness, menu);
        return true;
    }

    // 매서드 설명 : 선택한 탭에 따라서 분류 후, RecyclerView에 표시
    public void setupFreshnessRecycler() {
        // 탭에 따른 재료를 골라줌
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

        // 품목이 없을 경우 비었음을 표시
        TextView emptyView = findViewById(R.id.empty_view_recycler_freshness);
        RecyclerViewEmptySupport recycler = findViewById(R.id.recycler_freshness_freshness);

        // 유동적으로 재료를 표시하기 위해서 FlexboxLayoutManager 사용
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(cursor);

        // 화면에 꽉찼을 경우 다음줄로 넘어감 옵션 + 가로 방향으로 차례로 표시 옵션
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);

        // Adapter와 연결해서 검색한 재료를 RecyclerView에 표시
        recycler.setExpend(true);
        recycler.setEmptyView(emptyView);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    // 매서드 설명 : Toolbar에 버튼이 선택되었을 경우
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 뒤로가기 버튼
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        // 재료 추가하기 버튼 (Dialog 생성)
        else if(item.getItemId() == R.id.ingredient_add){
            GoodsDialog goodsDialog = new GoodsDialog(this);
            goodsDialog.setCancelable(false);
            goodsDialog.show();
        }
        // 재료 제거하기 버튼
        else if(item.getItemId() == R.id.ingredient_remove){
            // 제거하기 상황 여부에 따라 제거 아이콘 변경
            item.setIcon(remove_item?R.drawable.ic_eat:R.drawable.ic_cancel);

            // 제거하기 상황 여부에 따라 아이템 추가버튼 또는 제거 확인 버튼 표시
            Menu menu = toolbar.getMenu();
            MenuItem tmpItem = menu.findItem(R.id.ingredient_add);
            tmpItem.setVisible(remove_item);
            tmpItem = menu.findItem(R.id.ingredient_confirm);
            tmpItem.setVisible(!remove_item);

            remove_item = !remove_item;
            setupFreshnessRecycler();
        }
        // 제거 확인 버튼
        else if(item.getItemId() == R.id.ingredient_confirm){

            // 체크박스를 선택한 재료들만 골라서 제거
            for(int i = 0; i<FreshnessRecyclerAdapter.removeList.size(); i++){
                db.delete(DBContract.GoodsEntry.TABLE_NAME, DBContract.FridgeEntry._ID + "=?",
                        new String[]{FreshnessRecyclerAdapter.removeList.get(i).toString()});
            }

            // 재료가 1개 이상 선택되었을 경우
            if(FreshnessRecyclerAdapter.removeList.size() != 0){
                // 제거 상황 취소
                remove_item = false;

                // 제거 아이콘이 원래로 돌아가고 추가 아이콘이 다시 생김
                Menu menu = toolbar.getMenu();
                MenuItem tmpItem = menu.findItem(R.id.ingredient_add);
                tmpItem.setVisible(true);
                tmpItem = menu.findItem(R.id.ingredient_confirm);
                tmpItem.setVisible(false);
                Toast.makeText(getApplicationContext(), "재료가 삭제되었습니다", Toast.LENGTH_SHORT).show();

                // 정보 갱신
                setupFreshnessRecycler();
            }
            // 재료가 한개도선택되지 않았을 경우
            else{
                Toast.makeText(getApplicationContext(), "재료를 골라주세요", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    // 제거 상황 초기화
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        remove_item = false;
        finish();
    }

}