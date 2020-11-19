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

public class SectionActivity extends AppCompatActivity {

    private String fridge;
    private String section;
    private String storeState;
    private String fridgeCategory;

    private Toolbar toolbar;

    public static boolean remove_item = false;

    SQLiteDatabase db = MainActivity.db;

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

    public void setupFreshnessRecycler(String fridge, String section) {

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

    private void setupToolbar(String name, String storeState) {
        toolbar = findViewById(R.id.toolbar_section);

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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (item.getItemId() == R.id.option_remove) {
            AlertDialog alertDialog = new AlertDialog.Builder(SectionActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                    .setTitle("구역 제거")
                    .setIcon(R.drawable.ic_remove)
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
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
        else if (item.getItemId() == R.id.ingredient_add) {
            GoodsDialog goodsDialog = new GoodsDialog(this, fridge, section, fridgeCategory);
            goodsDialog.setCancelable(false);
            goodsDialog.show();
        }
        else if(item.getItemId() == R.id.ingredient_remove){
            remove_item = !remove_item;

            item.setIcon(remove_item?R.drawable.ic_cancel:R.drawable.ic_eat);
            Menu menu = toolbar.getMenu();
            MenuItem tmpItem = menu.findItem(R.id.ingredient_add);
            tmpItem.setVisible(!remove_item);
            tmpItem = menu.findItem(R.id.ingredient_confirm);
            tmpItem.setVisible(remove_item);

            setupFreshnessRecycler(fridge, section);
        }
        else if (item.getItemId() == R.id.ingredient_confirm) {
            remove_item = false;

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

            if(SectionRecyclerAdapter.removeList.size() != 0){
                Toast.makeText(getApplicationContext(), "재료가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "재료를 골라주세요", Toast.LENGTH_SHORT).show();
            }

            setupFreshnessRecycler(fridge, section);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        remove_item = false;
        finish();
    }
}
