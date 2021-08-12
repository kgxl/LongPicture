package com.colin.longpicture;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * create by colin
 * 2021/8/11
 */
public class PicCropTask extends AsyncTask<CropParams, Void, String> {

    private AssetManager assetManager;
    private ContentResolver contentResolver;

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    protected String doInBackground(CropParams... params) {
        try {
            CropParams cropParams = params[0];
            //加载原图
            Bitmap originBitmap = BitmapFactory.decodeStream(assetManager.open("2_01.jpg"));
            Matrix transform = cropParams.getTransform();
            RectF cropRectF = cropParams.getCropRectF();
            transform.postTranslate(-cropRectF.left, -cropRectF.top);
            float fitScale = cropParams.getFitScale();
            //对原图的缩放值
            float scale = SplicingUtils.getScale(transform) / fitScale;
            Matrix saveMatrix = new Matrix();
            saveMatrix.postScale(scale, scale);

            //图片裁剪框和原图的比例
            float s1 = originBitmap.getWidth() / cropRectF.width();
            float[] translations = SplicingUtils.getTranslations(transform);
            float left = translations[0] * s1;
            float top = translations[1] * s1;
            saveMatrix.postTranslate(left, top);

            Bitmap bitmap1 = Bitmap.createBitmap((int) (cropRectF.width() * s1 / scale), (int) (cropRectF.height() * s1 / scale),
                    Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap1);
            canvas.concat(saveMatrix);
            canvas.drawBitmap(originBitmap, 0,0, null);

//            Bitmap bitmap = Bitmap.createBitmap(originBitmap, (int) left, (int) top,
//                    (int) (cropRectF.width() * s1 / scale), (int) (cropRectF.height() * s1 / scale), saveMatrix, true);

//            Bitmap bitmap = Bitmap.createBitmap(originBitmap, (int) cropRectF.left, (int) cropRectF.top,
//                    (int) cropRectF.width(), (int) cropRectF.height(), transform, true);
            saveBitmap(bitmap1);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("PicCropTask", "onPostExecute: " + s);
    }

    private void saveBitmap(Bitmap bitmap) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "test" + System.currentTimeMillis()); // 文件的描述
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "test" + System.currentTimeMillis() + ".jpg"); // 设置保存到公共目录下的文件的名称
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*"); //文件的类型
        contentValues.put(MediaStore.Images.Media.TITLE, "test_" + System.currentTimeMillis());//文件的标题
        Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (insert != null) {
            try {
                OutputStream outputStream = contentResolver.openOutputStream(insert);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream); // 通过流写入
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("zwk", "文件么有找到");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("zwk", "流关闭异常");
            }
        }
    }
}
