package com.singh.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ortiz.touchview.TouchImageView;

public class FullScreenImage extends AppCompatActivity {

    private FloatingActionButton closeButton;
    private TouchImageView imageView;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        closeButton = findViewById(R.id.close_button);
        imageView = findViewById(R.id.fullscreen_imageview);
        imagePath = getIntent().getStringExtra("imagePath");
        if (!(imagePath == null || imagePath.equals(""))){
            Glide.with(this).load(imagePath).into(imageView);
        }
        closeButton.setOnClickListener(v -> {this.finish();});
    }
}