package com.example.najakneang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.najakneang.activity.MainFreshnessRecyclerItem;
import com.example.najakneang.activity.FridgeSectionRecyclerItem;
import com.example.najakneang.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FridgeSectionRecyclerAdapter
        extends RecyclerView.Adapter<FridgeSectionRecyclerAdapter.FridgeSectionRecyclerHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);
    }
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    private final FridgeSectionRecyclerItem[] items;
    private ArrayList<MainFreshnessRecyclerItem[]> itemList;
    private RecyclerView recyclerView;

    public FridgeSectionRecyclerAdapter(Context context
            , ArrayList<MainFreshnessRecyclerItem[]> itemList, FridgeSectionRecyclerItem[] items) {
        this.items = items;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public FridgeSectionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_section_fridge, viewGroup, false);
        return new FridgeSectionRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeSectionRecyclerHolder holder, int position){
        FridgeSectionRecyclerItem item = items[position];
        String name = item.getName();

        //MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(itemList.get(position));

        holder.sectionName.setText(name);
        holder.sectionPreview.setHasFixedSize(true);
        //holder.sectionPreview.setAdapter(adapter);
        holder.sectionPreview.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class FridgeSectionRecyclerHolder extends RecyclerView.ViewHolder {
        protected TextView sectionName;
        protected RecyclerView sectionPreview;

        public FridgeSectionRecyclerHolder(View view){
            super(view);

            this.sectionName = view.findViewById(R.id.section_name_section_fridge);
            this.sectionPreview = view.findViewById(R.id.sub_recycler_section_fridge);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != recyclerView.NO_POSITION){
                        mListener.onItemClick(v, pos);
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != recyclerView.NO_POSITION){
                        mLongListener.onItemLongClick(v, pos);
                    }
                    return true;
                }
            });
        }
    }
}
