package com.example.najakneang.model;

import android.graphics.Bitmap;

public class YoutubeContent {

//    public static final String YOUTUBE_API_KEY = "AIzaSyBvbUl4A4Y7lAbfAoUunccnorGm0YoqNfE";
    public static final String YOUTUBE_API_KEY = "AIzaSyAO2w4y965a2vd1V-2drGRtLlrUWzA1hGg";

    private String title;
    private String creator;
    private String videoId;
    private Bitmap thumbnail_bitmap;

    public YoutubeContent(String title, String creator, String videoId, Bitmap thumbnail_bitmap) {
        this.title = title;
        this.creator = creator;
        this.videoId = videoId;
        this.thumbnail_bitmap = thumbnail_bitmap;
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

    public Bitmap getBitmap() {return thumbnail_bitmap;}

    public void setBitmap(Bitmap thumbnail_bitmap) {this.thumbnail_bitmap = thumbnail_bitmap;}

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId){ this.videoId = videoId; }
}
