package com.example.najakneang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.najakneang.R;

import org.w3c.dom.Text;

public class FridgeActivity extends AppCompatActivity {

    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);
        title = (TextView) findViewById(R.id.fridgeTitle);

        String fridgeName = getIntent().getStringExtra("FRIDGE");
        title.setText(fridgeName);
        loadSection(fridgeName);
    }

    private void loadSection(String fridgeName) {

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}