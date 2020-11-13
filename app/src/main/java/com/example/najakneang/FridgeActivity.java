package com.example.najakneang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class FridgeActivity extends AppCompatActivity {

    private ArrayList<MainFreshnessRecyclerItem[]> itemList = new ArrayList<>();
    private FridgeSectionRecyclerItem[] item = {
            new FridgeSectionRecyclerItem("구역1"),
            new FridgeSectionRecyclerItem("구역2")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        itemList.add(
                new MainFreshnessRecyclerItem[] {
                        new MainFreshnessRecyclerItem("품목1", R.drawable.ic_launcher_background, 3)
                });
        itemList.add(
                new MainFreshnessRecyclerItem[] {
                        new MainFreshnessRecyclerItem("품목2", R.drawable.ic_launcher_background, 30)
                });

        RecyclerView sectionRecycler = findViewById(R.id.recycler_section_fridge);
        FridgeSectionRecyclerAdapter sectionAdapter = new FridgeSectionRecyclerAdapter(this, itemList, item);

        sectionAdapter.setOnItemClickListener(new FridgeSectionRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getApplicationContext(), FridgeSectionActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        sectionRecycler.setAdapter(sectionAdapter);
        sectionRecycler.setLayoutManager(manager);
    }

}