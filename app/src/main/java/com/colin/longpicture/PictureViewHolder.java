package com.colin.longpicture;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by colin
 * 2021/7/23
 */
public class PictureViewHolder extends RecyclerView.ViewHolder {

    public RatioFrameLayout mLayout;
    public ImageView mImage;

    public PictureViewHolder(@NonNull View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.item_picture_img);
        mLayout = itemView.findViewById(R.id.item_picture_frame);
    }
}
