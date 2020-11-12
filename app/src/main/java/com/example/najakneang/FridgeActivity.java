package com.example.najakneang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class FridgeActivity extends AppCompatActivity {

    private RecyclerView FridgeSection;
    private FridgeSectionRecyclerAdapter SectionAdapter;
    private ArrayList<MainFreshnessRecyclerItem[]> ItemList;
    private FridgeSectionRecyclerItem[] Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        FridgeSectionRecyclerItem[] prepareData2 = {
                new FridgeSectionRecyclerItem("구역1"), new FridgeSectionRecyclerItem("구역2")
        };
        ItemList = prepareData1();
        Item = prepareData2;
        FridgeSection = findViewById(R.id.recycler_fridge_section);
        SectionAdapter = new FridgeSectionRecyclerAdapter(FridgeActivity.this, ItemList, Item);
        LinearLayoutManager manager = new LinearLayoutManager(FridgeActivity.this);
        FridgeSection.setLayoutManager(manager);
        FridgeSection.setAdapter(SectionAdapter);
    }

    private ArrayList<MainFreshnessRecyclerItem[]> prepareData1() {
        ArrayList<MainFreshnessRecyclerItem[]> itemList = new ArrayList();

        //itemList.add(new MainFreshnessRecyclerItem[]("품목1", R.drawable.ic_launcher_background, 3));
        //itemList.add(new MainFreshnessRecyclerItem[]("품목 2", R.drawable.ic_launcher_background, 30));
        return itemList;
    }
}