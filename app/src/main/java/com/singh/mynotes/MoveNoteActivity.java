package com.singh.mynotes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singh.mynotes.RecyclerAdapters.MoveNoteRecyclerAdapter;
import com.singh.mynotes.viewmodel.FolderViewModel;

public class MoveNoteActivity extends AppCompatActivity {
    private FolderViewModel folderViewModel;
    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private MoveNoteRecyclerAdapter moveNoteRecyclerAdapter;
    private int folderId;
    private int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_note);
        folderId = getIntent().getIntExtra("folderId",-1);
        noteId = getIntent().getIntExtra("noteId",-1);
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        //setting for recycler view and adapter for that
        setRecyclerView();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_move_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderViewModel.folderListExcept(folderId).observe(this,folders -> {
            moveNoteRecyclerAdapter = new MoveNoteRecyclerAdapter(this,folders,noteId);
            recyclerView.setAdapter(moveNoteRecyclerAdapter);
        });
        this.setTitle("Move Note");
    }
}