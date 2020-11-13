package com.example.najakneang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FridgeSectionActivity extends AppCompatActivity {

    private final MainFreshnessRecyclerItem[] items = {
            new MainFreshnessRecyclerItem(
                    "품목 1", R.drawable.ic_launcher_background, 3),
            new MainFreshnessRecyclerItem(
                    "품목 2", R.drawable.ic_launcher_background, 30)
    };

    private final MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(items);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        setupFreshnessRecycler();
    }
    private void setupFreshnessRecycler() {
        RecyclerView recyclerView = findViewById(R.id.fridge_section_item);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    }
}
