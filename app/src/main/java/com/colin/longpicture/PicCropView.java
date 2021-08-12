package com.colin.longpicture;

import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * create by colin
 * 2021/8/6
 */
public class PicCropView extends View {


    private int mWidth, mHeight;
    private float mRatio = 1;
    private int rowCount = 3, colCount = 3;
    private Paint mPaint;
    //内边距
    private int mStrokeWidth = 1;
    private RectF mCropRectF, mBitmapRectF;

    private FloatEvaluator mFloatEvaluator;

    private Bitmap mBitmap;
    private Matrix mTransformMatrix;
    private CommonTouchHelper pointerInfo;

    private float mFitScale = 1f;


    public PicCropView(Context context) {
        super(context);
        init();
    }

    public PicCropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1f);
        mCropRectF = new RectF();
        mBitmapRectF = new RectF();
        mFloatEvaluator = new FloatEvaluator();
        mTransformMatrix = new Matrix();
        pointerInfo = new CommonTouchHelper(mTransformMatrix, false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w - getPaddingStart() - getPaddingEnd();
        this.mHeight = h - getPaddingTop() - getPaddingBottom();
        calculateRectSize(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制图片
        canvas.drawColor(Color.BLACK);
        if (mBitmap != null) {
            canvas.save();
            canvas.clipRect(mCropRectF);
            canvas.concat(mTransformMatrix);
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            canvas.restore();
        }
        canvas.save();
        mPaint.setStrokeWidth(mStrokeWidth);
        float drawH = (mCropRectF.height() - mStrokeWidth * (rowCount - 1)) / rowCount;
        for (int i = 1; i < rowCount; i++) {
            float y = mCropRectF.top + drawH * i + mStrokeWidth * (i - 1) + mStrokeWidth / 2f;
            canvas.drawLine(mCropRectF.left, y, mCropRectF.right, y, mPaint);
        }
        float drawW = (mCropRectF.width() - mStrokeWidth * (colCount - 1)) / colCount;
        for (int i = 1; i < colCount; i++) {
            float x = mCropRectF.left + drawW * i + mStrokeWidth * (i - 1) + mStrokeWidth / 2f;
            canvas.drawLine(x, mCropRectF.top, x, mCropRectF.bottom, mPaint);
        }
        mPaint.setStrokeWidth(1f);
        canvas.drawRect(mCropRectF, mPaint);
        canvas.restore();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return pointerInfo.onTouchEvent(event, this::invalidate, this::needRest);
    }

    private void needRest() {
        RectF tmp = new RectF(mBitmapRectF);
        mTransformMatrix.mapRect(tmp, mBitmapRectF);
        if (tmp.width() < mCropRectF.width() ||
                tmp.height() < mCropRectF.height()) {
            setFitCenterTransform();
        } else {
            float[] translations = SplicingUtils.getTranslations(mTransformMatrix);
            float dx = 0, dy = 0;
            if (tmp.left > mCropRectF.left) {
                dx = -translations[0] + mCropRectF.left;
            }
            if (tmp.top > mCropRectF.top) {
                dy = -translations[1] + mCropRectF.top;
            }
            if (tmp.right < mCropRectF.right) {
                dx = (mCropRectF.width() - tmp.width()) - translations[0] + mCropRectF.left;
            }
            if (tmp.bottom < mCropRectF.bottom) {
                dy = (mCropRectF.height() - tmp.height()) - translations[1] + mCropRectF.top;
            }
            mTransformMatrix.postTranslate(dx, dy);
        }
        invalidate();
    }

    private void calculateRectSize() {
        calculateRectSize(true);
    }

    private void calculateRectSize(boolean needAnimator) {
        int strokeWidth = (colCount - 1) * mStrokeWidth;
        int strokeHeight = (rowCount - 1) * mStrokeWidth;
        int width = mWidth - strokeWidth;
        int height = mHeight - strokeHeight;
        float rwMax = width * 1f / colCount;
        float rhMax = height * 1f / rowCount;
        float rh = rwMax / mRatio;
        float rw = rhMax * mRatio;
        float itemRectWidth;
        float itemRectHeight;
        if (rw <= rwMax) {
            itemRectWidth = rw;
            itemRectHeight = rw / mRatio;
        } else {
            itemRectHeight = rh;
            itemRectWidth = rh * mRatio;
        }
        float rectCropWidth = itemRectWidth * colCount + strokeWidth;
        float rectCropHeight = itemRectHeight * rowCount + strokeHeight;
        float left = (mWidth - rectCropWidth) / 2f + getPaddingStart();
        float top = (mHeight - rectCropHeight) / 2f + getPaddingTop();
        if (needAnimator) {
            //动画过渡
            animateSetRectF(left, top, left + rectCropWidth, top + rectCropHeight);
        } else {
            setCropRectFAndBitmapRect(new RectF(left, top,
                    left + rectCropWidth, top + rectCropHeight));
        }

    }

    private void animateSetRectF(float left, float top, float right, float bottom) {
        RectF oldRectF = new RectF(mCropRectF);
        RectF newRectF = new RectF(left, top, right, bottom);
        ValueAnimator animator = ValueAnimator.ofObject((TypeEvaluator<RectF>) (fraction, startValue, endValue)
                -> new RectF(mFloatEvaluator.evaluate(fraction, startValue.left, endValue.left),
                mFloatEvaluator.evaluate(fraction, startValue.top, endValue.top),
                mFloatEvaluator.evaluate(fraction, startValue.right, endValue.right),
                mFloatEvaluator.evaluate(fraction, startValue.bottom, endValue.bottom)), oldRectF, newRectF);
        animator.addUpdateListener(animation -> setCropRectFAndBitmapRect((RectF) animation.getAnimatedValue()));
        animator.start();
    }

    private void setCropRectFAndBitmapRect(RectF rectF) {
        mCropRectF.set(rectF);
        calculateFitScale();
        needRest();
        invalidate();
    }

    public void setRatio(float ratio) {
        ValueAnimator animator = ValueAnimator.ofFloat(mRatio, ratio);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            mRatio = (float) animation.getAnimatedValue();
            calculateRectSize(false);
        });
        animator.start();
    }

    public void addRowCount() {
        if (rowCount > 5)
            return;
        this.rowCount++;
        calculateRectSize();
    }

    public void deleteRowCount() {
        if (rowCount < 2)
            return;
        this.rowCount--;
        calculateRectSize();
    }

    public void addColCount() {
        if (colCount > 5)
            return;
        this.colCount++;
        calculateRectSize();
    }

    public void deleteColCount() {
        if (colCount < 2)
            return;
        this.colCount--;
        calculateRectSize();
    }

    public void setInnerStrokeWidth(int innerStrokeWidth) {
        this.mStrokeWidth = innerStrokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mBitmapRectF.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        calculateFitScale();
        setFitCenterTransform();
        invalidate();
    }

    private void setFitCenterTransform() {
        mTransformMatrix.reset();
        mTransformMatrix.postScale(mFitScale, mFitScale);
        float left = (mBitmapRectF.width() * mFitScale - mCropRectF.width()) / 2f;
        float top = (mBitmapRectF.height() * mFitScale - mCropRectF.height()) / 2f;
        mTransformMatrix.postTranslate(mCropRectF.left - left, mCropRectF.top - top);
    }

    private void calculateFitScale() {
        if (mBitmap == null)
            return;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float rw = mCropRectF.width() / width;
        float rh = mCropRectF.height() / height;
        mFitScale = Math.max(rw, rh);
    }

    public CropParams getCropParams() {
        CropParams cropParams = new CropParams(new Matrix(mTransformMatrix), new RectF(mCropRectF),
                new RectF(mBitmapRectF), rowCount, colCount, mRatio);
        cropParams.setFitScale(mFitScale);
        return cropParams;
    }


}
