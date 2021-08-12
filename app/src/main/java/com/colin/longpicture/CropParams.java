package com.colin.longpicture;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * create by colin
 * 2021/8/11
 * <p>
 * 分割的参数
 */
public class CropParams {

    private Matrix transform;
    private RectF cropRectF;
    private int rowCount, colCount;
    private float ratio;
    private RectF previewBitmapRect;
    private float fitScale;

    public CropParams(Matrix transform, RectF cropRectF,
                      RectF previewBitmapRect, int rowCount, int colCount, float ratio) {
        this.transform = transform;
        this.cropRectF = cropRectF;
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.ratio = ratio;
        this.previewBitmapRect = previewBitmapRect;
    }

    public float getFitScale() {
        return fitScale;
    }

    public void setFitScale(float fitScale) {
        this.fitScale = fitScale;
    }

    public Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    public RectF getCropRectF() {
        return cropRectF;
    }

    public void setCropRectF(RectF cropRectF) {
        this.cropRectF = cropRectF;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public RectF getPreviewBitmapRect() {
        return previewBitmapRect;
    }

    public void setPreviewBitmapRect(RectF previewBitmapRect) {
        this.previewBitmapRect = previewBitmapRect;
    }
}
