package com.singh.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ortiz.touchview.TouchImageView;

import java.io.File;

public class FullScreenImage extends AppCompatActivity {

    private AppCompatImageButton closeButton;
    private TouchImageView imageView;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        closeButton = findViewById(R.id.close_btn);
        imageView = findViewById(R.id.fullscreen_imageview);
        imagePath = getIntent().getStringExtra("imagePath");
        if (!(imagePath == null || imagePath.equals(""))){
            Glide.with(this).load(imagePath).into(imageView);
            File file = new File(imagePath);
            if (file.exists()){ // if file exist for this path then only add listener to share file
                imageView.setOnLongClickListener(v ->{
                    //logic for long click to share photo with other applications
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    Uri fileUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider",file);
                    intent.setDataAndType(fileUri,"image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,fileUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share Image:"));

                    return false;
                });
            }
        }
        closeButton.setOnClickListener(v -> {this.finish();});
    }
}