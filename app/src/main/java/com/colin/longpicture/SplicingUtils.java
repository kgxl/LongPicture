package com.colin.longpicture;

import android.graphics.Matrix;

/**
 * create by colin
 * 2021/7/21
 */
public class SplicingUtils {

    private static final float[] values = new float[9];

    public static float getScale(Matrix matrix) {
        matrix.getValues(values);
        float scalex = values[Matrix.MSCALE_X];
        float skewy = values[Matrix.MSKEW_Y];
        return (float) Math.sqrt(scalex * scalex + skewy * skewy);
    }

    public static float[] getTranslations(Matrix matrix) {
        matrix.getValues(values);
        return new float[]{values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]};
    }
}
