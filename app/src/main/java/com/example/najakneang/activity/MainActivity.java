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

//    SQLiteDatabase DB; //DB 객체 생성
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        DB = setupDateBase(); // DB 준비
//        initTable(); // 테이블 실행
//        insertFakeData();

        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

//        insertFakeData(); 한개의 데이터 삽입 되있음

        setupFreshnessRecycler();
        //setupFridgeViewPager();
        //setupRecommendRecycler();
        //setonClickMaskLayout();
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
                null
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

    // 임의의 가데이터 입력 함수
    private void insertFakeData() {
        ContentValues values = new ContentValues();
        values.put(DBContract.GoodsEntry.COLUMN_NAME, "품목 1");
        values.put(DBContract.GoodsEntry.COLUMN_QUANTITY, 1);
        values.put(
                DBContract.GoodsEntry.COLUMN_REGISTDATE,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put(DBContract.GoodsEntry.COLUMN_EXPIREDATE, "2021-05-23");
        values.put(DBContract.GoodsEntry.COLUMN_TYPE, "타입 1");
        values.put(DBContract.GoodsEntry.COLUMN_IMAGE, R.drawable.ic_launcher_background);
        values.put(DBContract.GoodsEntry.COLUMN_FRIDGE, "냉장고 1");
        values.put(DBContract.GoodsEntry.COLUMN_SECTION, "구역 1");
        db.insert(DBContract.GoodsEntry.TABLE_NAME, null, values);
    }
//
//    // 아이템 데이터 입력합수
//    private void insertItemData(String name, int quantity, String expireDate, String type, String fridge, String storageState, String section) {
//        if(DB != null){
//            String current = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            String sqlItemInsert = "INSERT OR REPLACE INTO Items (NAME, QUANTITY, REGISTDATE, EXPIREDATE, TYPE, FRIDGE, STORESTATE, SECTION) VALUES ('"+
//                    name        + "', '" +
//                    quantity    + "', '" +
//                    current     + "', '" +
//                    expireDate  + "', '" +
//                    type        + "', '" +
//                    fridge      + "', '" +
//                    storageState+ "', '" +
//                    section     + "');";
//            DB.execSQL(sqlItemInsert);
//        }
//    }
//
//    // 저장구역 데이터 입력함수
//    private void insertSectionData(String section, String fridge, String state){
//        if(DB != null){
//            String sqlSectionInsert = "INSERT OR REPLACE INTO Section (FRIDGE, SECTION, STORESTATE) VALUES ('" +
//                    section + "', '" +
//                    fridge + "', '" +
//                    state   + "');";
//            DB.execSQL(sqlSectionInsert);
//        }
//    }
//
//    // 냉장고 데이터 입력함수
//    private void insertFridgeData(String fridge){
//        if(DB != null){
//            String sqlFridgeInsert = "INSERT OR REPLACE INTO Fridge (FRIDGE) VALUES ('" +
//                    fridge+"');";
//            DB.execSQL(sqlFridgeInsert);
//        }
//
//    }
//
//    //
//    private void initTable(){
//        String sqlItems = "CREATE TABLE IF NOT EXISTS Items (" +
//                    "NAME "         + "TEXT," +
//                    "QUANTITY "     + "INTEGER NOT NULL," +
//                    "REGISTDATE "   + "TEXT," +
//                    "EXPIREDATE "   + "TEXT," +
//                    "TYPE "         + "TEXT," +
//                    "FRIDGE "       + "TEXT," +
//                    "STORESTATE "   + "TEXT," +
//                    "SECTION "      + "TEXT"  + ");";
//
//        String sqlSections = "CREATE TABLE IF NOT EXISTS Sections (" +
//                "SECTION "     +"TEXT," +
//                "FRIDGE "    +"TEXT," +
//                "STORESTATE "   +"TEXT" + ");";
//
//        String sqlFridges = "CREATE TABLE IF NOT EXISTS Fridges (" +
//                "FRIDGE " + "TEXT);";
//
//        Log.e("e","error init");
//        DB.execSQL(sqlItems);
//        DB.execSQL(sqlSections);
//        DB.execSQL(sqlFridges);
//    }
//
//    private String[] loadFreshnessData() {
//        String[] returnData = new String[0];
//        if(DB != null){
//            try {
//                String sqlQuery = "SELECT * FROM Items";
//
//                Cursor cursor = null;
//
//                cursor = DB.rawQuery(sqlQuery, null);
//                // 전체 목록 이름/ 등록일 목록 만들고 넘기기
//                returnData = new String[cursor.getCount()];
//                int c = 0;
//
//                while(cursor.moveToNext()) {
//                    String name = cursor.getString(0);
//                    String date = cursor.getString(3);
//                     returnData[c++]= name+"/"+date;
//                }
//            } catch (SQLException se){
//                Log.e("e", se.toString());
//            }
//        }
//        return returnData;
//    }
//
//    private SQLiteDatabase setupDateBase() {
//        SQLiteDatabase db = null;
//
//        File file = new File(getFilesDir(), DBHelper.DB_NAME);
//        try{
//            db = SQLiteDatabase.openOrCreateDatabase(file, null);
//        }catch (SQLException se){
//            se.printStackTrace();
//        }
//
//        if(db == null){
//            Log.e("e","error setup");
//        }
//
//        return db;
//    }
//

    /**
     * 신선도 위험품목 설정
     * TODO: 3개 정도만 사용하기
     */
//    private void setupFreshnessRecycler() {
//        RecyclerView recyclerView = findViewById(R.id.recycler_freshness_main);
//        String[] freshnessData = loadFreshnessData();
//        MainFreshnessRecyclerItem[] items = new MainFreshnessRecyclerItem[freshnessData.length];
//
//        for(int i = 0; i<freshnessData.length;i++){
//            String[] info = freshnessData[i].split("/");
//            items[i] = new MainFreshnessRecyclerItem(info[0], R.drawable.ic_launcher_background, (int) remainDate(info[1]));
//        }
//
//        MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(items);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(
//                        this, LinearLayoutManager.HORIZONTAL, false
//                )
//        );
//    }

    /**
     * 냉장고 슬라이더 설정
     * TODO: 현재 자신의 냉장고 목록에 따라 조정되야됨
     * TODO: 클릭하면 그 냉장고 화면으로 갈수 있어야됨
     * TODO: 맨 마지막에 추가버튼 있어야됨
     * 필요에 따라 item 클래스 생성해서 이름, 사진, 냉장고 id 같은것들 담을수있게해야할듯?
     */
    private void setupFridgeViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewpager_fridge_main);
        // 가데이터
        String[] items = {
                "냉장고 1",
                "냉장고 2",
                "김치 냉장고 1",
                "팬트리 1"
        };

        MainFridgeViewPagerAdapter adapter = new MainFridgeViewPagerAdapter(items);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }


    private void setupRecommendRecycler() {
        ArrayList<YoutubeContent> data = new ArrayList<>();

        try {
            Thread passingThread = new Thread(() -> {
                // TODO: 재료 선택에 성공했다면 ingredient에 감자 대신 다른 것을 넣을 것!!
                getYoutubeContents(data, "감자");
            });
            passingThread.start();
            passingThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_recommend_main);
        MainRecommendRecyclerAdapter adapter = new MainRecommendRecyclerAdapter(data);

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
     * TODO: 오늘의 메뉴 선정
     * 재사용성을 위해 수정!!
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