package com.example.najakneang.model;

import android.graphics.Bitmap;

// 파일 설명 : 유튜브 Data API를 통해서 해당 재료의 영상 목록을 받아오는 파일
// 파일 주요기능 : API를 통해서 영상의 제목, 채널명, 비디오 아이디를 받아옴

// 클래스 설명 : 영상의 제목, 채널명, 비디오 아이디, 비디오 아이디로 받아온 썸네일을 저장하는 객체
// 이를 RecommendRecyclerAdapter에 활용하여 유튜브 영상 목록을 띄움
public class YoutubeContent {

    // Youtube Data API에 접속할 서버키 (API할당량 문제 때문에 두개 번갈아가면서 사용)
    public static final String YOUTUBE_API_KEY = "AIzaSyBvbUl4A4Y7lAbfAoUunccnorGm0YoqNfE";
//    public static final String YOUTUBE_API_KEY = "AIzaSyAO2w4y965a2vd1V-2drGRtLlrUWzA1hGg";

    private String title;
    private String creator;
    private String videoId;
    private Bitmap thumbnail_bitmap;


    // 생성자
    public YoutubeContent(String title, String creator, String videoId, Bitmap thumbnail_bitmap) {
        this.title = title;
        this.creator = creator;
        this.videoId = videoId;
        this.thumbnail_bitmap = thumbnail_bitmap;
    }

    // getter와 setter들
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
