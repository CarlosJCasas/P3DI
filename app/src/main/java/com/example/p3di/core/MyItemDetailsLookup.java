package com.example.p3di.core;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3di.UI.CustomAdapter;

public class MyItemDetailsLookup extends ItemDetailsLookup<String> {
    private RecyclerView recyclerView;

    public MyItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(),e.getY());
        if(view != null){
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if(holder instanceof CustomAdapter.ViewHolder){
//                return ((CustomAdapter.ViewHolder) holder);
            }
        }
        return null;
    }
}
