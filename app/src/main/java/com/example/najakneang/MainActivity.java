package com.example.najakneang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFreshnessRecycler();
        setupOnClickFreshnessLayout();
        setupFridgeViewPager();
        setupRecommendRecycler();
    }

    /**
     * 신선도 위험품목 설정
     * TODO: 이후에 신선도가 적은 품목을 받아와서 수정하면 됨.
     */
    private void setupFreshnessRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler_freshness_main);
        // 가데이터
        MainFreshnessRecyclerItem[] items = {
                new MainFreshnessRecyclerItem(
                        "품목 1", R.drawable.ic_launcher_background, 3),
                new MainFreshnessRecyclerItem(
                        "품목 2", R.drawable.ic_launcher_background, 30)
        };

        MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.HORIZONTAL, false
                )
        );
    }

    private void setupOnClickFreshnessLayout() {
        RelativeLayout freshness = findViewById(R.id.layout_freshness_main);
        freshness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FreshnessActivity.class);
                startActivity(intent);
            }
        });
    }

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
                "김치 냉장고 1"
        };

        MainFridgeViewPagerAdapter adapter = new MainFridgeViewPagerAdapter(items);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }


    /**
     * 오늘의 추천메뉴 설정
     * TODO: 오늘의 메뉴 선정
     * TODO: 유튜브에서 검색 결과 상위 몇개를 가져올 수 있어야됨
     * TODO: 클릭 시 유튜브로 넘어갈 수 있게.
     */
    private void setupRecommendRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler_recommend_main);
        // 가데이터
        MainRecommendRecyclerItem[] items = {
                new MainRecommendRecyclerItem(
                        "유튜브 영상 제목 1", "크리에이터 1", R.drawable.img_thumbnail_example_1),
                new MainRecommendRecyclerItem(
                        "유튜브 영상 제목 2", "크리에이터 2", R.drawable.img_thumbnail_example_2),
                new MainRecommendRecyclerItem(
                        "유튜브 영상 제목 3", "크리에이터 3", R.drawable.img_thumbnail_example_3)
        };

        MainRecommendRecyclerAdapter adapter = new MainRecommendRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }
}