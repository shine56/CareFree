package com.example.a73233.carefree.util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;

public class BigPhotoViewer extends BaseActivity {
    private ZoomImageView bigPhoto;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo_viewer);

        ReviseStatusBar(TRANSPARENT_WHITE);
        bigPhoto = findViewById(R.id.big_photo);
        imagePath = getIntent().getStringExtra("imagePath");
        Glide.with(this).load(imagePath).error(R.mipmap.find_photo_fail).into(bigPhoto);

        bigPhoto.setOnClickListener(v -> onBackPressed());
    }
}
