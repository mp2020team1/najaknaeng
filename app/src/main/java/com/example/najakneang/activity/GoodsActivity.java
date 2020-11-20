package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.najakneang.R;
import com.example.najakneang.adapter.RecommendRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.GoodsDialog;
import com.example.najakneang.model.YoutubeContent;

import java.util.ArrayList;

/**
 * 클래스 설명
 * 품목의 대한 정보와 품목에 대한 레시피 추천을 보여주는 Activity
 * Toolbar에서 품목 삭제나 품목 수정, 네이버 쇼핑에 품목 검색이 가능하다.
 */
public class GoodsActivity extends AppCompatActivity {

    private final SQLiteDatabase db = MainActivity.db;
    private Cursor cursor;

    private String nameStr;
    private String quantityStr;
    private String expireDate;
    private String typeStr;
    private String fridge;
    private String section;
    private String state;
    private long goodsId;

    // 품목의 id를 intent에서 받아온 뒤, 각종 setup함수 실행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        goodsId = getIntent().getLongExtra("GOODSID",0);

        getGoodsCursor(goodsId);
        setupToolbar();
        setupGoodsView();
    }

    // Toolbar를 받아온 품목 Cursor에 따라 setup
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_goods);

        String title = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 받아온 품목 Cursor에 따라 각종 TextView와 ImageView 설정
     * MainActivity의 Youtube Content 가져오기 함수를 사용해
     * 해당 품목의 영상 3가지를 RecyclerView에 넣어줌.
     */
    public void setupGoodsView() {
        TextView name = findViewById(R.id.name_goods);
        TextView location = findViewById(R.id.location_goods);
        ImageView image = findViewById(R.id.image_goods);
        TextView quantity = findViewById(R.id.text_quantity_goods);
        TextView remain = findViewById(R.id.text_remain_goods);
        TextView type = findViewById(R.id.text_type_goods);
        RecyclerView recyclerView = findViewById(R.id.recycler_recommend_goods);

        nameStr = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        expireDate = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_EXPIREDATE));
        typeStr = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_TYPE));
        fridge = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_FRIDGE));
        section = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_SECTION));
        state = cursor.getString(cursor.getColumnIndex(DBContract.SectionEntry.COLUMN_STORE_STATE));
        quantityStr = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_QUANTITY));
        name.setText(nameStr);
        name.setTextColor(Color.parseColor(DBContract.GoodsEntry.getRemainColor(state, DBContract.GoodsEntry.getRemain(expireDate))));
        location.setText(fridge + " / " + section);
        image.setImageResource(DBContract.GoodsEntry.typeIconMap.get(typeStr));
        quantity.setText(quantityStr);
        remain.setText(DBContract.GoodsEntry.getRemain(expireDate) + "일");
        type.setText(typeStr);

        ArrayList<YoutubeContent> contents = new ArrayList<>();
        Handler handler = new Handler();

        new Thread(() -> {
            MainActivity.getYoutubeContents(contents, nameStr);

            handler.post(() -> {
                RecommendRecyclerAdapter adapter = new RecommendRecyclerAdapter(contents);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                );
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            });
        }).start();
    }

    // intent로 받아온 id에 따라 데이터베이스에서 부르는 과정
    public void getGoodsCursor(long id) {

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
                        " WHERE " + DBContract.GoodsEntry.TABLE_NAME + "." + DBContract.GoodsEntry._ID + " = " + id + " LIMIT 1";

        cursor = db.rawQuery(sql,null);

        cursor.moveToFirst();
    }

    // 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goods, menu);
        return true;
    }

    /**
     * 각 메뉴가 눌렸을때 반응 설정
     * 뒤로가기 버튼: 뒷버튼 누르기
     * 품목 삭제 버튼: Dialog를 통해 정말 삭제할 것인지 확인 후, 삭제 혹은 취소
     * 구매 버튼: Intent를 통해 해당 품목의 네이버 쇼핑 페이지로 넘어감.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.ingredient_remove){
            AlertDialog alertDialog = new AlertDialog.Builder(GoodsActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                    .setTitle("재료 제거")
                    .setIcon(R.drawable.ic_remove)
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            db.delete(DBContract.GoodsEntry.TABLE_NAME,  BaseColumns._ID + "=?",
                                    new String[]{String.valueOf(goodsId)});
                            finish();
                            Toast.makeText(getApplicationContext(), "재료가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니요", null)
                    .show();
            return true;
        } else if (item.getItemId() == R.id.ingredient_edit) {
            GoodsDialog goodsDialog = new GoodsDialog(this,goodsId, nameStr, quantityStr, expireDate);
            goodsDialog.setCancelable(false);
            goodsDialog.show();
            return true;
        } else if (item.getItemId() == R.id.purchase) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "https://search.shopping.naver.com/search/all?query=" +
                    nameStr + "&cat_id=&frm=NVSHATC" ));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}