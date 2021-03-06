package com.singh.mynotes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.singh.mynotes.models.Note;
import com.singh.mynotes.viewmodel.NoteViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity  {

    private Button cancelButton;
    private Button saveButton;

    NoteViewModel noteViewModel;
    private int folderId;
    private EditText titleEditText;
    private EditText subjectEditText;
    private EditText contentEditText;
    private final int REQUEST_CODE_CAMERA = 102;
    private final int REQUEST_CODE_GALLERY = 103;
    private final int REQUEST_CODE_STORAGE_PERMISSION = 105;
    private final int REQUEST_CODE_CAMERA_PERMISSION = 106;

    private ImageView imageView;
    private Bitmap bitmap;
    private InputMethodManager inputMethodManager;
    private Uri imageUri;
    private String tempImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        folderId = getIntent().getIntExtra("folderId",-1);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        SimpleDateFormat formatter= new SimpleDateFormat("dd/M/yyyy hh:mm aa");

        // Cancel button
        cancelButton = findViewById(R.id.cancel_button_create_note);
        cancelButton.setOnClickListener(v -> { this.finish();});

        folderId = getIntent().getIntExtra("folderId",1);
        saveButton = findViewById(R.id.save_button_create_note);
        titleEditText = findViewById(R.id.note_title_create);
        subjectEditText = findViewById(R.id.note_subject_create);
        contentEditText = findViewById(R.id.note_detail_create);
        imageView = findViewById(R.id.image_note_create);
        //following function bind buttons of camera and gallery
        bindCameraGalleryButton();

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String subject = subjectEditText.getText().toString().trim();
            String detail = contentEditText.getText().toString().trim();
            if (title.isEmpty()) {
                titleEditText.setError("Title cannot be empty");
                titleEditText.requestFocus();
                return;
            }
            if (subject.isEmpty()) {
                subjectEditText.setError("Subject cannot be empty");
                subjectEditText.requestFocus();
                return;
            }
            if (detail.isEmpty()) {
                contentEditText.setError("Detail cannot be empty");
                contentEditText.requestFocus();
                return;
            }
            String dateTime = formatter.format(new Date());
            Note newNote = new Note(folderId, title,subject, detail, dateTime);
            if (bitmap!=null) {
                String imagePath = saveImage(bitmap);
                if (imagePath != null) {
                    newNote.setImagePath(imagePath);
                    Log.d("PATH_IMAGE",imagePath);
                }
            }
            noteViewModel.insert(newNote);
            this.finish();
        });
    }


    //+++++++++  following code is for getting photo from camera and gallery ++++++++++++++++++++++
    private void bindCameraGalleryButton(){

        findViewById(R.id.camera_button_create).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
                return;
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    imageUri = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
                }
            }
        });

        findViewById(R.id.gallery_button_create).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,REQUEST_CODE_GALLERY);
        });

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "tempImage";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        //path of temp file that is use to delete the original file
        //after getting and converting image to bitmap
        tempImagePath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backIntent) {
        super.onActivityResult(requestCode, resultCode, backIntent);

        if (requestCode == REQUEST_CODE_CAMERA){  //back from camera
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
//                    following lines are to delete temp file for saving image from camera
                    File file = new File(tempImagePath);
                    file.delete();
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CODE_GALLERY){ //back from gallery
            if (resultCode == RESULT_OK) {
                try {
                    imageUri = backIntent.getData();
                    //following to convert from uri to bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    //following is to get and show image through URI in image view
                    //imv1.setImageURI(uri);
                    imageView.setImageBitmap(bitmap);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    //----------------- Camera and gallery code ends ----------------------------------------------

    private String saveImage(Bitmap finalBitmap) {
        String root = CreateNoteActivity.this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
        File myDir = new File(root);
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Image"+ timeStamp +"_.webp";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 60, out);
            out.flush();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null){
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        super.onUserInteraction();
    }
}