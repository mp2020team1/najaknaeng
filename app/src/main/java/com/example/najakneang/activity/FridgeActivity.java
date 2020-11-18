package com.example.najakneang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FreshnessRecyclerAdapter;
import com.example.najakneang.adapter.FridgeRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.model.GoodsDialog;
import com.example.najakneang.model.SectionDialog;

public class FridgeActivity extends AppCompatActivity {

    private final SQLiteDatabase db = MainActivity.db;
    private String fridgeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        Intent intent = getIntent();
        fridgeName = intent.getStringExtra("FRIDGE");
        String fridgeCategory = intent.getStringExtra("CATEGORY");

        loadSection(fridgeName);

        setupToolbar(fridgeName, fridgeCategory);
    }

    @Override
    protected void onResume(){
        super.onResume();

        loadSection(fridgeName);
    }

    private void setupToolbar(String name, String category) {
        Toolbar toolbar = findViewById(R.id.toolbar_fridge);

        toolbar.setTitle(name);
        toolbar.setSubtitle(category);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void loadSection(String name) {
        String[] projection = {
                BaseColumns._ID,
                DBContract.SectionEntry.COLUMN_NAME,
                DBContract.SectionEntry.COLUMN_FRIDGE,
                DBContract.SectionEntry.COLUMN_STORE_STATE
        };

        String selection = DBContract.SectionEntry.COLUMN_FRIDGE + " = ?";
        String[] selectionArgs = { name };

        Cursor sectionCursor = db.query(
                DBContract.SectionEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        RecyclerView recyclerView = findViewById(R.id.recycler_section_fridge);
        FridgeRecyclerAdapter adapter = new FridgeRecyclerAdapter(sectionCursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fridge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.option_add:
                SectionDialog sectionDialog = new SectionDialog(this, fridgeName);
                sectionDialog.setCancelable(false);
                sectionDialog.show();
                return true;
            case R.id.option_remove:
                AlertDialog alertDialog = new AlertDialog.Builder(FridgeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                        .setTitle("냉장고 제거")
                        .setIcon(R.drawable.ic_remove)
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}