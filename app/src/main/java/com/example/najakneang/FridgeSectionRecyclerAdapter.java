package com.example.najakneang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class FridgeSectionRecyclerHolder extends RecyclerView.ViewHolder {
    protected TextView sectionName;
    protected RecyclerView sectionPreview;

    public FridgeSectionRecyclerHolder(View view){
        super(view);

        this.sectionName = view.findViewById(R.id.section_name_section_fridge);
        this.sectionPreview = view.findViewById(R.id.sub_recycler_section_fridge);
    }
}

public class FridgeSectionRecyclerAdapter
        extends RecyclerView.Adapter<FridgeSectionRecyclerHolder> {

    private final FridgeSectionRecyclerItem[] items;
    private ArrayList<MainFreshnessRecyclerItem[]> itemList;

    public FridgeSectionRecyclerAdapter(Context context, ArrayList<MainFreshnessRecyclerItem[]> itemList, FridgeSectionRecyclerItem[] items) {
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

        MainFreshnessRecyclerAdapter adapter = new MainFreshnessRecyclerAdapter(itemList.get(position));

        holder.sectionName.setText(name);
        holder.sectionPreview.setHasFixedSize(true);
        holder.sectionPreview.setAdapter(adapter);
        holder.sectionPreview.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

}
