/*
 * Copyright (c) 2015 Everimaging Co., Ltd.
 */

package com.colin.longpicture;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioFrameLayout extends FrameLayout {

    private float mRatio;
    private boolean mBaseWidth = true;
    private float mScale = 1f;

    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0.0) {
            int width;
            int height;
            if (mBaseWidth) {
                // set the image views size
                width = MeasureSpec.getSize(widthMeasureSpec);
                height = (int) (width / mRatio);
            } else {
                height = MeasureSpec.getSize(heightMeasureSpec);
                width = (int) (height * mRatio);
            }
            super.onMeasure(MeasureSpec.makeMeasureSpec((int) (width * mScale), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) (height * mScale), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * width / height
     *
     * @param ratio width / height ratio.
     */
    public void setRatio(float ratio) {
        this.mRatio = ratio;
        requestLayout();
    }

    public void scale(float scale) {
        this.mScale = scale;
    }
}
