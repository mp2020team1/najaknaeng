package com.example.najakneang;

import androidx.recyclerview.widget.RecyclerView;

public class FridgeSectionRecyclerItem {
    private String name;

    public FridgeSectionRecyclerItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
