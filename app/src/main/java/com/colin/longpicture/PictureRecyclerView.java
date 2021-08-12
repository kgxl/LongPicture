package com.colin.longpicture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by colin
 * 2021/7/23
 */
public class PictureRecyclerView extends RecyclerView {

    private Matrix mTransformMatrix;

    public PictureRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public PictureRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTransformMatrix = new Matrix();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
//        canvas.scale(0.5f,0.5f);
        super.dispatchDraw(canvas);
        canvas.restore();

    }

    public void setScale(float scale) {
        mTransformMatrix.setScale(scale, scale);
        invalidate();
    }

    public void postScale(float scale) {
        mTransformMatrix.postScale(scale, scale);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }
}
