package com.colin.longpicture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String tag = "MainActivity";
    private PictureRecyclerView mRecyclerView;
    private PictureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.main_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        ArrayList<Picture> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int color = Color.rgb(random.nextInt(255),
                    random.nextInt(255), random.nextInt(255));
            Picture p = new Picture(800,
                    1200 - random.nextInt(1000), color);
            data.add(p);
        }
        mAdapter = new PictureAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new Callback());
        helper.attachToRecyclerView(mRecyclerView);
        mAdapter.setItemClickListener((position, picture) -> {
            scaleBig(position);
        });
    }

    private void scaleBig(int position) {
        ValueAnimator animator = ValueAnimator.ofFloat(0.5f, 1);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRecyclerView.smoothScrollToPosition(position);
            }
        });
        animator.addUpdateListener(animation -> {
            mRecyclerView.scrollToPosition(position);
            mAdapter.scale((Float) animation.getAnimatedValue());
        });
        animator.start();

    }


    public void selectFile(View view) {
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("image/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(intent, 100);
        ValueAnimator animator = ValueAnimator.ofFloat(0.3f, 1);
        animator.addUpdateListener(animation -> mAdapter.scale((Float) animation.getAnimatedValue()));
        animator.start();
    }

    public void scaleView(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0.3f);

        animator.addUpdateListener(animation -> mAdapter.scale((Float) animation.getAnimatedValue()));
        animator.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
        }
    }
}