package com.example.najakneang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.najakneang.R;

public class FridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        String test = getIntent().getStringExtra("FRIDGE");
        Log.i("FRIDGE", test);
    }
}