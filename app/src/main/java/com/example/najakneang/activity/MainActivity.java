package com.example.najakneang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.najakneang.adapter.MainFreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.adapter.MainFridgeViewPagerAdapter;
import com.example.najakneang.adapter.MainRecommendRecyclerAdapter;
import com.example.najakneang.model.YoutubeContent;
import com.example.najakneang.R;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        //insertFakeData(); //첫 시행이라면 데이터를 삽입해주세요! (한번만)

        setupFreshnessRecycler();
        setupFridgeViewPager();
        setupRecommendRecycler();
        setonClickMaskLayout();
    }

    // 임의의 가데이터 입력 함수
    private void insertFakeData() {
        ContentValues values = new ContentValues();
        values.put(DBContract.GoodsEntry.COLUMN_NAME, "감자");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "2021-01-23");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "타입 1");
        values.put(DBContract.GoodsEntry.COLUMN_IMAGE, R.drawable.ic_launcher_background);
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 1");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
    }

    private void setupFreshnessRecycler() {
        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE,
                DBContract.GoodsEntry.COLUMN_IMAGE
        };

        Cursor cursor = db.query(
                DBContract.GoodsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                DBContract.GoodsEntry.COLUMN_EXPIREDATE
        );

        RecyclerView recyclerView = findViewById(R.id.recycler_freshness_main);
        MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(cursor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.HORIZONTAL, false
                )
        );
    }

    /**
     * 냉장고 슬라이더 설정
     * TODO: 현재 자신의 냉장고 목록에 따라 조정되야됨
     * TODO: 클릭하면 그 냉장고 화면으로 갈수 있어야됨
     * TODO: 맨 마지막에 추가버튼 있어야됨
     * 필요에 따라 item 클래스 생성해서 이름, 사진, 냉장고 id 같은것들 담을수있게해야할듯?
     */
    private void setupFridgeViewPager() {

        String[] projection = {
                BaseColumns._ID,
                DBContract.FridgeEntry.COLUMN_NAME
        };

        Cursor cursor = db.query(
                DBContract.FridgeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        ViewPager2 viewPager = findViewById(R.id.viewpager_fridge_main);
        MainFridgeViewPagerAdapter adapter = new MainFridgeViewPagerAdapter(cursor);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }


    private void setupRecommendRecycler() {
        ArrayList<YoutubeContent> contents = new ArrayList<>();
        try {


            Thread passingThread = new Thread(() -> {
                String[] projection = {
                        BaseColumns._ID,
                        DBContract.GoodsEntry.COLUMN_NAME
                };

                Cursor cursor = db.query(
                        DBContract.GoodsEntry.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1",
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                Log.e("e","make cursor");
                cursor.moveToNext();
                String ingredient =  cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
                getYoutubeContents(contents, ingredient);
            });
            passingThread.start();
            passingThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_recommend_main);
        MainRecommendRecyclerAdapter adapter = new MainRecommendRecyclerAdapter(contents);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    public void setonClickMaskLayout() {
        FrameLayout mask = findViewById(R.id.layout_mask_main);
        mask.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), FreshnessActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 오늘의 추천메뉴 설정
     * 재사용성을 위해 수정했음.
     */
    private void getYoutubeContents(ArrayList<YoutubeContent> data, String ingredient) {
        String api_key = YoutubeContent.YOUTUBE_API_KEY;
        ingredient += " 레시피";

        try {
            // Youtube Data Api를 이용하여 영상 검색 결과 정보가 담긴 json을 가져옴
            String youtube = "https://www.googleapis.com/youtube/v3/search?q=" + ingredient +
                    "&key=" + api_key   +
                    "&part=snippet"     +
                    "&regionCode=KR"    +
                    "&type=video"       +
                    "&maxResults=3";
            URL url = new URL(youtube);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 접속 성공 확인
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 웹의 response을 줄단위로 읽어 String에 하나하나 저장함
                InputStream is = conn.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder builder = new StringBuilder();

                while (sc.hasNext()) {
                    builder.append(sc.nextLine());
                }
                String receiveMsg = builder.toString();

                // JSON 파싱
                try {
                    JSONObject jsonObject = new JSONObject(receiveMsg);
                    JSONArray youtubeArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < youtubeArray.length(); i++) {
                        JSONObject youtubeObject = youtubeArray.getJSONObject(i);

                        // Youtube 제목, 채널명 받아오기
                        JSONObject tmpObject;
                        tmpObject = youtubeObject.getJSONObject("snippet");
                        String title = tmpObject.getString("title");
                        String creator = tmpObject.getString("channelTitle");

                        // VideoId 받아오기
                        tmpObject = youtubeObject.getJSONObject("id");
                        String videoId = tmpObject.getString("videoId");

                        // 썸네일 받아오기
                        String thumbnail_url_str = "https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg";
                        URL thumbnail_url = new URL(thumbnail_url_str);
                        Bitmap thumbnail = BitmapFactory.decodeStream(thumbnail_url.openStream());

                        // RecyclerView에 dataset 추가
                        data.add(
                                new YoutubeContent(title, creator, videoId, thumbnail)
                        );
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}