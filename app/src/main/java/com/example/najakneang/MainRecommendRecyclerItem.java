package com.example.najakneang;

import android.graphics.Bitmap;

public class MainRecommendRecyclerItem {
    private String title;
    private String creator;
    private String videoId;
    private Bitmap thumbnail_bitmap;

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

    public Bitmap getBitmap() {return thumbnail_bitmap;}

    public void setBitmap(Bitmap thumbnail_bitmap) {this.thumbnail_bitmap = thumbnail_bitmap;}

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId){ this.videoId = videoId; }
}
