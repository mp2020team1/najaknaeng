package com.example.najakneang;

public class MainRecommendRecyclerItem {
    private String title;
    private String creator;
    private int thumbnail;

    public MainRecommendRecyclerItem(String title, String creator, int thumbnail) {
        this.title = title;
        this.creator = creator;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
