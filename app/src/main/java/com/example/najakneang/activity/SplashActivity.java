package com.example.najakneang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.najakneang.R;

// 클래스 설명: 시작 전 오프닝 화면을 보여주는 Activity
public class SplashActivity extends AppCompatActivity {

    /**
     * 메소드 설명
     * Activity를 시작하고 Handler를 통해 3초 대기한 후, MainActivity로 넘어감
     * 이때, 이 Activity는 finish()되어 Back Button으로 돌아올 수 없게함.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}