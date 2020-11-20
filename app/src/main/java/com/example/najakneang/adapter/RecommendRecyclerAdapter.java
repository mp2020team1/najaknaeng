package com.example.najakneang.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.model.YoutubeContent;
import com.example.najakneang.R;

import java.util.ArrayList;

// 파일 설명 : 추천 레시피 RecyclerView 와 보여줄 데이터를 연결하는 Adapter class
// 파일 주요 기능 : RecyclerView의 각 홀더에 데이터 및 이벤트 연결

// 클래스 설명 : RecyclerView의 각 View를 저장하는 Holder 객체 class
class RecommendRecyclerHolder extends RecyclerView.ViewHolder {
    protected final View view;
    protected final ImageView thumbnail;
    protected final TextView title;
    protected final TextView creator;

    // 메서드 설명 : Holder 객체를 생성하고 item_recycler_recommend 레이아웃의 View와 연결
    public RecommendRecyclerHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.title = view.findViewById(R.id.title_item_recommend);
        this.creator = view.findViewById(R.id.creator_item_recommend);
        this.thumbnail = view.findViewById(R.id.thumbnail_item_recommend);
    }
}

// 클래스 설명 : RecyclerView와 데이터를 연결하는 Adapter class
public class RecommendRecyclerAdapter
        extends RecyclerView.Adapter<RecommendRecyclerHolder> {

    private final ArrayList<YoutubeContent> data;

    // 메서드 설명 : 입력받은 유튜브 영상 정보를 담은 객체를 저장
    public RecommendRecyclerAdapter(ArrayList<YoutubeContent> data) {
        this.data = data;
    }

    // 메서드 설명 : Holder 객체를 item_recycler_recommend 레이아웃에 따라 생성
    @NonNull
    @Override
    public RecommendRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_recommend, viewGroup, false);

        return new RecommendRecyclerHolder(view);
    }

    // 메서드 설명 : 생성된 Holder 객체와 유튜브 정보 객체를 연결
    @Override
    public void onBindViewHolder(@NonNull RecommendRecyclerHolder holder, int position) {
        // Recycler 각 Item에 제목, 채널명, 썸네일 지정
        YoutubeContent datum = data.get(position);
        String title = datum.getTitle();
        String creator = datum.getCreator();
        Bitmap thumbnail_bitmap = datum.getBitmap();

        holder.title.setText(title);
        holder.creator.setText(creator);
        holder.thumbnail.setImageBitmap(thumbnail_bitmap);

        // Item 클릭시, 해당 유튜브 영상으로 이동
        holder.view.setOnClickListener(view -> {
            String videoId = datum.getVideoId();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://youtube.com/watch?v=" + videoId ));
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
