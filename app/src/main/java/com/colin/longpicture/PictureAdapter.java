package com.colin.longpicture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * create by colin
 * 2021/7/23
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureViewHolder> {

    private final List<Picture> mData;
    private float mScale = 1f;
    private ItemClickListener itemClickListener;

    public PictureAdapter(@NonNull List<Picture> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picture, parent, false);
        return new PictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Picture picture = mData.get(position);
        float ratio = picture.width * 1f / picture.height;
        holder.mLayout.setRatio(ratio);
        holder.mImage.setBackgroundColor(picture.color);
        holder.mLayout.scale(mScale);
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.itemClick(position, mData.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void scale(float scale) {
        mScale = scale;
        notifyDataSetChanged();
    }

    public void swap(int from, int to) {
        Collections.swap(mData, from, to);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
