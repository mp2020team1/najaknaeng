package com.example.najakneang.activity;

import androidx.appcompat.app.AlertDialog;
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
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.najakneang.adapter.FreshnessRecyclerAdapter;
import com.example.najakneang.db.DBContract;
import com.example.najakneang.db.DBHelper;
import com.example.najakneang.adapter.MainFridgeViewPagerAdapter;
import com.example.najakneang.adapter.RecommendRecyclerAdapter;
import com.example.najakneang.model.RecyclerViewEmptySupport;
import com.example.najakneang.model.YoutubeContent;
import com.example.najakneang.R;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Scanner;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime = 0;
    public static int fridge_id = 0;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDB();
        setupRecommendRecycler(); //할당량 문제로 임시로 onCreate()에 생성 -> onResume()으로 추후 변경

        Button credit = findViewById(R.id.credit);
        credit.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                    .setTitle("크레딧")
                    .setIcon(R.drawable.ic_kitchen)
                    .setPositiveButton("감사합니다", null)
                    .setMessage(getString(R.string.credit))
                    .show();
        });

    }

    //RecyclerView 정보 갱신
    @Override
    protected void onResume(){
        super.onResume();

        setupFreshnessRecycler();
        setupFridgeViewPager();
    }

    //두번 뒤로가기해야 꺼지게하기
    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() <= backPressedTime + 2000) {
            finish();
            return;
        }
        backPressedTime = System.currentTimeMillis();
        Toast.makeText(this, "뒤로 가기를 한 번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
    }

    private void setDB(){
        try {
            Thread dbOpenThread = new Thread(() -> {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                db = dbHelper.getWritableDatabase();
//                insertFakeData();
            });
            dbOpenThread.start();
            dbOpenThread.join();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // 임의의 가데이터 입력 함수
    private void insertFakeData() {
        db.delete(DBContract.GoodsEntry.TABLE_NAME, null, null);
        db.delete(DBContract.FridgeEntry.TABLE_NAME,null,null);
        db.delete(DBContract.SectionEntry.TABLE_NAME, null, null);
        ContentValues values = new ContentValues();

        values.put(DBContract.FridgeEntry.COLUMN_NAME, "냉장고 1");
        values.put(DBContract.FridgeEntry.COLUMN_CATEGORY, "냉장고");
        db.insert(DBContract.FridgeEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.FridgeEntry.COLUMN_NAME, "김치냉장고 1");
        values.put(DBContract.FridgeEntry.COLUMN_CATEGORY, "김치 냉장고");
        db.insert(DBContract.FridgeEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.FridgeEntry.COLUMN_NAME, "팬트리 1");
        values.put(DBContract.FridgeEntry.COLUMN_CATEGORY, "팬트리");
        db.insert(DBContract.FridgeEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.SectionEntry.COLUMN_NAME, "구역 1");
        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, "냉장");
        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.SectionEntry.COLUMN_NAME, "구역 2");
        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, "냉동");
        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.SectionEntry.COLUMN_NAME, "구역 3");
        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, "실온");
        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.SectionEntry.COLUMN_NAME, "구역 1");
        values.put(DBContract.SectionEntry.COLUMN_FRIDGE, "김치냉장고 1");
        values.put(DBContract.SectionEntry.COLUMN_STORE_STATE, "냉장");
        db.insert(DBContract.SectionEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.GoodsEntry.COLUMN_NAME, "감자");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "20201121");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "채소");
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 1");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.GoodsEntry.COLUMN_NAME, "토마토");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "20201023");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "채소");
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 1");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.GoodsEntry.COLUMN_NAME, "방어");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "20021223");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "수산");
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 2");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
        values.clear();

        values.put(DBContract.GoodsEntry.COLUMN_NAME, "120년 김치만 연구한 전통 장인의 정성이 가득 담긴 굉장히 엄청난 배추김치");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "20211123");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "반찬");
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "김치냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 1");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
        values.clear();
    }

    private void setupFreshnessRecycler() {
        String sql = "SELECT " + DBContract.GoodsEntry.TABLE_NAME+"."+BaseColumns._ID+", "+
                DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_NAME +", "+
                DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_TYPE +", "+
                DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_EXPIREDATE +", "+
                DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_SECTION +", "+
                DBContract.SectionEntry.TABLE_NAME +"."+DBContract.SectionEntry.COLUMN_STORE_STATE +
                " FROM " + DBContract.GoodsEntry.TABLE_NAME +
                " INNER JOIN " + DBContract.SectionEntry.TABLE_NAME +
                " ON " + DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_SECTION +
                " = " + DBContract.SectionEntry.TABLE_NAME +"."+DBContract.SectionEntry.COLUMN_NAME + " AND " +
                DBContract.GoodsEntry.TABLE_NAME+"."+ DBContract.GoodsEntry.COLUMN_FRIDGE +
                " = " + DBContract.SectionEntry.TABLE_NAME+"."+ DBContract.SectionEntry.COLUMN_FRIDGE +
                " ORDER BY " + DBContract.GoodsEntry.TABLE_NAME +"."+DBContract.GoodsEntry.COLUMN_EXPIREDATE + " LIMIT 3";

        Cursor cursor = db.rawQuery(sql,null);

        TextView emptyView = findViewById(R.id.empty_view_recycler_freshness_main);
        RecyclerViewEmptySupport recyclerView = findViewById(R.id.recycler_freshness_main);
        FreshnessRecyclerAdapter adapter = new FreshnessRecyclerAdapter(cursor);
        recyclerView.setExpend(true);
        recyclerView.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.HORIZONTAL, false
                )
        );
    }


    public void setupFridgeViewPager() {
        String[] projection = {
                BaseColumns._ID,
                DBContract.FridgeEntry.COLUMN_NAME,
                DBContract.FridgeEntry.COLUMN_CATEGORY
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
        viewPager.setCurrentItem(fridge_id);

        CircleIndicator3 indicator = findViewById(R.id.circle_indicator_viewpager_fridge_main);
        indicator.setViewPager(viewPager);
    }

    private void setupRecommendRecycler() {
        String[] projection = {
                BaseColumns._ID,
                DBContract.GoodsEntry.COLUMN_NAME
        };

        Cursor cursor = db.query(
                DBContract.GoodsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                "RANDOM()",
                "1"
        );

        ArrayList<YoutubeContent> contents = new ArrayList<>();
        Handler handler = new Handler();

        new Thread(() -> {
            if (cursor.moveToNext()){
                String ingredient =  cursor.getString(cursor.getColumnIndex(DBContract.GoodsEntry.COLUMN_NAME));
                getYoutubeContents(contents, ingredient);
            }

            handler.post(() -> {
                TextView emptyView = findViewById(R.id.empty_view_recycler_recommend_main);
                RecyclerViewEmptySupport recyclerView = findViewById(R.id.recycler_recommend_main);
                RecommendRecyclerAdapter adapter = new RecommendRecyclerAdapter(contents);

                recyclerView.setEmptyView(emptyView);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                );
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            });
        }).start();
    }

    public void onClickMaskLayout(View view) {
        Intent intent = new Intent(getApplicationContext(), FreshnessActivity.class);
        startActivity(intent);
    }

    public static void getYoutubeContents(ArrayList<YoutubeContent> data, String ingredient) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.i("Youtube", conn.getResponseCode() + "인 접속오류!"); //지우지 말것
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}