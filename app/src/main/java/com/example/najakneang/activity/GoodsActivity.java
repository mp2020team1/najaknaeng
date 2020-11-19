package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        goodsId = getIntent().getLongExtra("GOODSID",0);

        getGoodsCursor(goodsId);
        setupToolbar();
        setupGoodsView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_goods);

        String title = cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goods, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.ingredient_remove){
            db.delete(DBContract.GoodsEntry.TABLE_NAME,  BaseColumns._ID + "=?",
                    new String[]{String.valueOf(goodsId)});
            finish();
            Toast.makeText(getApplicationContext(), "재료가 삭제되었습니다", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.ingredient_edit){
            GoodsDialog goodsDialog = new GoodsDialog(this,goodsId, nameStr, quantityStr, expireDate);
            goodsDialog.setCancelable(false);
            goodsDialog.show();
        } else if(item.getItemId() == R.id.purchase){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "https://search.shopping.naver.com/search/all?query=" +
                    nameStr + "&cat_id=&frm=NVSHATC" ));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}