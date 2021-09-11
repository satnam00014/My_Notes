package com.singh.mynotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.singh.mynotes.models.Note;
import com.singh.mynotes.viewmodel.NoteViewModel;

import java.io.File;
import java.net.URI;

public class EditNoteActivity extends AppCompatActivity {

    private int noteId;
    private Note note;
    private EditText titleEditText, subjectEditText, detailEditText;
    private NoteViewModel noteViewModel;
    private ImageView imageView;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        noteId = getIntent().getIntExtra("noteId",-1);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        note = noteViewModel.getNote(noteId);
        if (note==null){
            Toast.makeText(EditNoteActivity.this,"ERROR WHILE GETTING NOTE",Toast.LENGTH_SHORT).show();
            return;
        }
        titleEditText = findViewById(R.id.note_title_edit);
        titleEditText.setText(note.getNoteTitle());
        subjectEditText = findViewById(R.id.note_subject_edit);
        subjectEditText.setText(note.getNoteSubject());
        detailEditText = findViewById(R.id.note_detail_edit);
        detailEditText.setText(note.getNoteContent());
        if (note.getImagePath()!=null){
            Log.d("PATH_EDIT_IMAGE",note.getImagePath());
            File file = new File(note.getImagePath());
            if (file.exists()){
                imageView = findViewById(R.id.imageview_edit);
                Glide.with(this).load(note.getImagePath()).into(imageView);
                imageView.setOnClickListener(v -> {
                    //Logic to open full screen zoomable image.
                    Intent intent = new Intent(this,FullScreenImage.class);
                    intent.putExtra("imagePath",note.getImagePath());
                    startActivity(intent);
                });
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
        findViewById(R.id.cancel_button_edit_note).setOnClickListener(v -> {
            this.finish();
        });
        findViewById(R.id.save_button_edit_note).setOnClickListener(v -> {
            saveThisNote();
        });
    }

    private void saveThisNote(){
        String title = titleEditText.getText().toString().trim();
        String subject = subjectEditText.getText().toString().trim();
        String detail = detailEditText.getText().toString().trim();
        if (title.isEmpty()) {
            titleEditText.setError("First Name cannot be empty");
            titleEditText.requestFocus();
            return;
        }
        if (subject.isEmpty()) {
            subjectEditText.setError("Last name cannot be empty");
            subjectEditText.requestFocus();
            return;
        }
        if (detail.isEmpty()) {
            detailEditText.setError("Last name cannot be empty");
            detailEditText.requestFocus();
            return;
        }
        note.setNoteTitle(title);
        note.setNoteSubject(subject);
        note.setNoteContent(detail);
        noteViewModel.update(note);
        this.finish();
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