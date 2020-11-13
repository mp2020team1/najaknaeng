package com.example.najakneang;

import androidx.recyclerview.widget.RecyclerView;

public class FridgeSectionRecyclerItem {
    private String section;
    //private RecyclerView sectionPreview;

    public FridgeSectionRecyclerItem(String section) {
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section=section;
    }

    /*public RecyclerView getSectionPreview() {
        return sectionPreview;
    }

    public void setSectionPreview(RecyclerView sectionPreview) {
        this.sectionPreview=sectionPreview;
    }*/
}
