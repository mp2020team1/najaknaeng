package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FridgeRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.RecyclerViewEmptySupport;
import com.example.najakneang.model.SectionDialog;
// 파일 설명 : 선택한 냉장고를 보여주는 엑티비티
// 파일 주요 기능 : 선택한 냉장고가 가지고 있는 구역을 보여주고 각 구역마다 저장된 품목 3가지를 보여준다.
//              메뉴를 이용하여 구역의 추가및 삭제, 냉장고 삭제가능

// 클래스 설명 : 선택한 냉장고를 보여주는 FridgeActivity class
public class FridgeActivity extends AppCompatActivity {

    private final SQLiteDatabase db = MainActivity.db;
    private String fridgeName;
    private String fridgeCategory;

    // 메서드 설명 : 각종 메서드 실행과 인텐트값 가져오기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        // 인텐트에서 냉장고 이름과 분류를 가져온다.
        Intent intent = getIntent();
        fridgeName = intent.getStringExtra("FRIDGE");
        fridgeCategory = intent.getStringExtra("CATEGORY");

        loadSection(fridgeName);

        setupToolbar(fridgeName, fridgeCategory);
    }

    // 메서드 설명 : 냉장고내 구역 정보 갱신기능
    @Override
    protected void onResume(){
        super.onResume();

        loadSection(fridgeName);
    }

    // 메서드 설명 : Toolbar에 받은 이름과 분류를 표기하고 설정
    private void setupToolbar(String name, String category) {
        Toolbar toolbar = findViewById(R.id.toolbar_fridge);

        toolbar.setTitle(name);
        toolbar.setSubtitle(category);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 메서드 설명 : 해당 냉장고 안에 있는 구역들에 대한 정보를 받아와 설정
    public void loadSection(String name) {
        // Innerjoin을 사용하여 Sections와 Fridge 테이블을 연동하여 정보를 받는다.
        String sql = "SELECT " + DBContract.SectionEntry.TABLE_NAME+"."+ BaseColumns._ID+", "+
                DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_NAME +", "+
                DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_FRIDGE +", "+
                DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_STORE_STATE+", "+
                DBContract.FridgeEntry.TABLE_NAME+"."+ DBContract.FridgeEntry.COLUMN_CATEGORY+
                " FROM " + DBContract.SectionEntry.TABLE_NAME +
                " INNER JOIN " + DBContract.FridgeEntry.TABLE_NAME +
                " ON " + DBContract.SectionEntry.TABLE_NAME +"."+DBContract.SectionEntry.COLUMN_FRIDGE +
                " = " + DBContract.FridgeEntry.TABLE_NAME +"."+DBContract.FridgeEntry.COLUMN_NAME +
                " WHERE " + DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_FRIDGE + " = '" + name+"'";

        Cursor sectionCursor = db.rawQuery(sql, null);

        TextView emptyView = findViewById(R.id.empty_view_recycler_section_fridge);
        RecyclerViewEmptySupport recyclerView = findViewById(R.id.recycler_section_fridge);
        FridgeRecyclerAdapter adapter = new FridgeRecyclerAdapter(sectionCursor);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // 메서드 설명 : 초기 옵션메뉴를 설정한다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fridge, menu);
        return true;
    }

    // 메서드 설명 : 옵션 아이템 선택에 따라 다른 이벤트 실행
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 선택한 옵션에 따라서 작동
        switch (item.getItemId()) {
            // 뒤로가기 버튼
            case android.R.id.home:
                onBackPressed();
                return true;
            // 구역 추가 버튼
            case R.id.option_add:
                SectionDialog sectionDialog = new SectionDialog(this, fridgeName, fridgeCategory);
                sectionDialog.setCancelable(false);
                sectionDialog.show();
                return true;
            // 냉장고 삭제 버튼
            case R.id.option_remove:
                AlertDialog alertDialog = new AlertDialog.Builder(FridgeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                        .setTitle("냉장고 제거")
                        .setIcon(R.drawable.ic_remove)
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            // 해당 버튼 클릭시 DB에서도 삭제 이때, 냉장고 내의 구역, 재료들도 모두 삭제
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db.delete(DBContract.GoodsEntry.TABLE_NAME, DBContract.GoodsEntry.COLUMN_FRIDGE + "=?",
                                        new String[]{fridgeName});
                                db.delete(DBContract.SectionEntry.TABLE_NAME, DBContract.SectionEntry.COLUMN_FRIDGE + "=?",
                                        new String[]{fridgeName});
                                db.delete(DBContract.FridgeEntry.TABLE_NAME, DBContract.FridgeEntry.COLUMN_NAME + "=?",
                                        new String[]{fridgeName});
                                setResult(RESULT_OK);
                                finish();
                                Toast.makeText(getApplicationContext(), "냉장고가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("아니요", null)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로 가기 클릭시 제거 상황 초기화
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}