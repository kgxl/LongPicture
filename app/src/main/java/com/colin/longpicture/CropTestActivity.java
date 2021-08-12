package com.colin.longpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class CropTestActivity extends AppCompatActivity {

    private PicCropView cropView;
    private EditText etWidth;
    private EditText etHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_test);
        cropView = findViewById(R.id.crop_view);
        findViewById(R.id.add_col).setOnClickListener(v -> cropView.addColCount());
        findViewById(R.id.delete_col).setOnClickListener(v -> cropView.deleteColCount());
        findViewById(R.id.add_row).setOnClickListener(v -> cropView.addRowCount());
        findViewById(R.id.delete_row).setOnClickListener(v -> cropView.deleteRowCount());
        etWidth = findViewById(R.id.et_width);
        etHeight = findViewById(R.id.et_height);
        findViewById(R.id.bt_ratio)
                .setOnClickListener(v -> setCropRatio());
        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cropView.setInnerStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        try {
            InputStream open = getAssets().open("2_01.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeStream(open, null, options);
            cropView.post(() -> cropView.setBitmap(bitmap));

        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.bt_save)
                .setOnClickListener(v -> {
                    startSaveTask();
                });
    }

    private void startSaveTask() {
        PicCropTask task = new PicCropTask();
        task.setAssetManager(getAssets());
        task.setContentResolver(getContentResolver());
        task.execute(cropView.getCropParams());
    }

    private void setCropRatio() {
        String w = etWidth.getText().toString();
        String h = etHeight.getText().toString();
        if (TextUtils.isEmpty(w) || TextUtils.isEmpty(h))
            return;
        float wf = Float.parseFloat(w);
        float hf = Float.parseFloat(h);
        cropView.setRatio(wf / hf);
    }
}