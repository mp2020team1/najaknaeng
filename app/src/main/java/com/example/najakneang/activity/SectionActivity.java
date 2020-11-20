package com.example.najakneang.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.najakneang.adapter.SectionRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.R;
import com.example.najakneang.model.GoodsDialog;
import com.example.najakneang.model.RecyclerViewEmptySupport;

import java.util.ArrayList;

// 파일 설명 : 선택한 구역을 보여주는 엑티비티
// 파일 주요 기능 : 선택한 구역에 저장한 재료을 보여주며 메뉴를 통해 재료의 추가 및 삭제가능

// 클래스 설명 : 선택한 구역을 보여주는 SectionActivity class
public class SectionActivity extends AppCompatActivity {

    private String fridge;
    private String section;
    private String storeState;
    private String fridgeCategory;

    private Toolbar toolbar;

    public static boolean remove_item = false;

    SQLiteDatabase db = MainActivity.db;

    // 메서드 설명 : 각종 메서드 실행과 인텐트값 가져오기
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        fridge = getIntent().getStringExtra("FRIDGE");
        section = getIntent().getStringExtra("SECTION");
        storeState = getIntent().getStringExtra("STORESTATE");
        fridgeCategory = getIntent().getStringExtra("CATEGORY");
        setupFreshnessRecycler(fridge, section);
        setupToolbar(section, storeState);
    }

    // 메서드 설명 : 구역내 재료 정보 갱신기능
    @Override
    protected void onResume(){
        super.onResume();

        setupFreshnessRecycler(fridge, section);
    }

    // 메서드 설명 : 해당 구역 안에 있는 재료들에 대한 정보를 받아와 설정
    public void setupFreshnessRecycler(String fridge, String section) {

        // Innerjoin을 사용하여 Goods와 Sections 테이블을 연동하여 정보를 받는다.
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

        TextView emptyView = findViewById(R.id.empty_view_recycler_section);
        RecyclerViewEmptySupport recyclerView = findViewById(R.id.recycler_section);
        SectionRecyclerAdapter adapter = new SectionRecyclerAdapter(cursor);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
    }

    // 메서드 설명 : Toolbar에 받은 이름과 저장상태를 표기하고 설정
    private void setupToolbar(String name, String storeState) {
        toolbar = findViewById(R.id.toolbar_section);

        toolbar.setTitle(name);
        toolbar.setSubtitle(storeState);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 메서드 설명 : 초기 옵션메뉴를 설정한다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_section, menu);
        return true;
    }

    // 메서드 설명 : 옵션 아이템 선택에 따라 다른 이벤트 실행
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 뒤로가기 버튼
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        // 구역 제거 버튼
        else if (item.getItemId() == R.id.option_remove) {
            AlertDialog alertDialog = new AlertDialog.Builder(SectionActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                    .setTitle("구역 제거")
                    .setIcon(R.drawable.ic_remove)
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Innerjoin을 사용하여 세가지 테이블을 연동하여 정보를 받아옴
                            String sql =
                                    "SELECT " + DBContract.GoodsEntry.TABLE_NAME + "." + BaseColumns._ID +
                                            " FROM " + DBContract.GoodsEntry.TABLE_NAME +
                                            " INNER JOIN " + DBContract.SectionEntry.TABLE_NAME +
                                            " INNER JOIN " + DBContract.FridgeEntry.TABLE_NAME +
                                            " ON " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                                            DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME + " AND " +
                                            DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_SECTION + " = " +
                                            DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " AND " +
                                            DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry.COLUMN_FRIDGE + " = " +
                                            DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_FRIDGE +
                                            " WHERE " + DBContract.FridgeEntry.TABLE_NAME + "." + DBContract.FridgeEntry.COLUMN_NAME + " = " + "\"" + fridge + "\"" + " AND " +
                                            DBContract.SectionEntry.TABLE_NAME + "." + DBContract.SectionEntry.COLUMN_NAME + " = " + "\"" + section + "\"";

                            Cursor cursor = db.rawQuery(sql, null);

                            ArrayList<Long> removeList = new ArrayList<Long>();

                            // 구역이 제거되면 그 안에 있는 재료들도 삭제되도록함
                            while(cursor.moveToNext()){
                                removeList.add(cursor.getLong(
                                        cursor.getColumnIndex(DBContract.GoodsEntry._ID)));
                            }

                            for(int i = 0; i<removeList.size(); i++){
                                db.delete(DBContract.GoodsEntry.TABLE_NAME, DBContract.GoodsEntry._ID + "=?",
                                        new String[]{removeList.get(i).toString()});
                            }

                            db.delete(DBContract.SectionEntry.TABLE_NAME, DBContract.SectionEntry.COLUMN_FRIDGE + "=? AND " +
                                            DBContract.SectionEntry.COLUMN_NAME + "=?",
                                    new String[]{fridge, section});
                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(getApplicationContext(), "구역이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니요", null)
                    .show();

            return true;
        }
        // 재료 추가 버튼
        else if (item.getItemId() == R.id.ingredient_add) {
            GoodsDialog goodsDialog = new GoodsDialog(this, fridge, section, fridgeCategory);
            goodsDialog.setCancelable(false);
            goodsDialog.show();
        }
        // 재료 제거 버튼
        else if (item.getItemId() == R.id.ingredient_remove){
            remove_item = !remove_item;
            Menu menu = toolbar.getMenu();
            MenuItem tmpItem;

            item.setIcon(remove_item ? R.drawable.ic_cancel : R.drawable.ic_eat);
            tmpItem = menu.findItem(R.id.ingredient_add);
            tmpItem.setVisible(!remove_item);
            tmpItem = menu.findItem(R.id.ingredient_confirm);
            tmpItem.setVisible(remove_item);

            setupFreshnessRecycler(fridge, section);
        }
        // 옵션 확인 버튼
        else if (item.getItemId() == R.id.ingredient_confirm) {
            if (SectionRecyclerAdapter.removeList.size() != 0){
                Menu menu = toolbar.getMenu();
                MenuItem tmpItem = menu.findItem(R.id.ingredient_add);
                tmpItem.setVisible(true);
                tmpItem = menu.findItem(R.id.ingredient_confirm);
                tmpItem.setVisible(false);

                for (int i = 0; i<SectionRecyclerAdapter.removeList.size(); i++) {
                    db.delete(
                            DBContract.GoodsEntry.TABLE_NAME,
                            DBContract.FridgeEntry._ID + "=?",
                            new String[]{ SectionRecyclerAdapter.removeList.get(i).toString() }
                    );
                }
                Toast.makeText(getApplicationContext(), "재료가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                remove_item = false;
            } else {
                Toast.makeText(getApplicationContext(), "재료를 골라주세요", Toast.LENGTH_SHORT).show();
            }

            setupFreshnessRecycler(fridge, section);
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로 가기 클릭시 제거 상황 초기화
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        remove_item = false;
        finish();
    }
}
