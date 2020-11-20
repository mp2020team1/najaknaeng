package com.example.najakneang.model;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.najakneang.activity.FreshnessActivity;

// 파일 설명 : Recycler가 비었을 때 처리하기 위한 커스텀 클래스

/**
 * 클래스 설명
 * RecyclerView를 상속받아 지역변수로 emptyView를 설정하여
 * RecyclerView에 내장된 onChange에 따라 adapter의 item 갯수를 파악하여
 * emptyView를 보여줄지 결정해줌.
 * extend는 adapter에 count가 1부터 시작하는 adapter가 있기 때문에
 * 따로 bool값으로 설정 가능하게 해놓았음.
 */
public class RecyclerViewEmptySupport extends RecyclerView {

    private View emptyView;
    boolean expend = false;

    // Recycler에 onChange에 넣어줄 커스텀 Observer
    private final AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            int count = adapter.getItemCount();
            if (adapter != null && emptyView != null) {
                if (!expend && count == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                } else if (expend && count == 1) {
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    // 생성자
    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    // 생성자
    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 생성자
    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // overirde setter
    // Adapter를 설정할때 Observer를 우리가 선언한 커스텀 Observer로 변환해줌.
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    // setter
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    // setter
    public void setExpend(boolean expend) { this.expend = expend; }
}
