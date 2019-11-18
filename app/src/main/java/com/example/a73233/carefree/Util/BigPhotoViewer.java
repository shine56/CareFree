package com.example.a73233.carefree.Util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a73233.carefree.R;

public class BigPhotoViewer extends AppCompatActivity {
    private ImageView bigPhoto;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo_viewer);
        bigPhoto = findViewById(R.id.big_photo);
        imagePath = getIntent().getStringExtra("imagePath");
        Glide.with(this).load(imagePath).error(R.mipmap.find_photo_fail).into(bigPhoto);

        bigPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
