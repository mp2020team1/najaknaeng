package com.example.najakneang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.TextView;

import com.example.najakneang.R;
import com.example.najakneang.adapter.FridgeRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;

import org.w3c.dom.Text;

public class FridgeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);
        title = (TextView) findViewById(R.id.fridgeTitle);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();


        String fridgeName = getIntent().getStringExtra("FRIDGE");
        title.setText(fridgeName);
        loadSection(fridgeName);
    }

    private void loadSection(String fridgeName) {

        String[] projection = {
            BaseColumns._ID,
            DBContract.SectionEntry.COLUMN_NAME,
            DBContract.SectionEntry.COLUMN_FRIDGE,
            DBContract.SectionEntry.COLUMN_STORE_STATE
        };

        Cursor cursor = db.query(
                DBContract.SectionEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        RecyclerView recyclerView = findViewById(R.id.recycler_section_fridge);
        FridgeRecyclerAdapter adapter = new FridgeRecyclerAdapter(cursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}