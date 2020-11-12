package com.example.najakneang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FridgeSectionRecyclerAdapter
        extends RecyclerView.Adapter<FridgeSectionRecyclerAdapter.FridgeSectionRecyclerHolder> {

    private final FridgeSectionRecyclerItem[] items;
    private ArrayList<MainFreshnessRecyclerItem[]> ItemList;
    private Context context;

    public FridgeSectionRecyclerAdapter(Context context, ArrayList<MainFreshnessRecyclerItem[]> ItemList, FridgeSectionRecyclerItem[] items) {
        this.items = items;
        this.ItemList = ItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public FridgeSectionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fridge_part, parent, false);
        return new FridgeSectionRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeSectionRecyclerHolder holder, int position){
        //MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(ItemList.get(position));
        FridgeSectionRecyclerItem item = items[position];
        String section = item.getSection();
        //RecyclerView sectionPreview = item.getSectionPreview();

        holder.section.setText(section);
        holder.sectionPreview.setHasFixedSize(true);
        holder.sectionPreview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //holder.sectionPreview.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class FridgeSectionRecyclerHolder extends RecyclerView.ViewHolder {
        protected TextView section;
        protected RecyclerView sectionPreview;

        public FridgeSectionRecyclerHolder(View view){
            super(view);

            this.section = view.findViewById(R.id.section_name_fridge_section);
            this.sectionPreview = (RecyclerView)view.findViewById(R.id.recycler_section_items_fridge_section);
        }
    }


}
