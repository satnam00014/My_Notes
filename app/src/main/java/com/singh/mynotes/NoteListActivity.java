package com.singh.mynotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singh.mynotes.RecyclerAdapters.NotesRecyclerAdapter;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.viewmodel.NoteViewModel;

public class NoteListActivity extends AppCompatActivity {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FolderWithNotes folderWithNotes;
    private NoteViewModel noteViewModel;
    private InputMethodManager inputMethodManager;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        folderId = getIntent().getIntExtra("folderId",-1);

        findViewById(R.id.add_note_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateNoteActivity.class);
            intent.putExtra("folderId",folderId);
            startActivity(intent);
        });

        //setting for recycler view and adapter for that
        setRecyclerView();
        Toast.makeText(this,"Folder id is : "+folderId,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.notes_list_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch_notes);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //create filter class before apply below line otherwise app will crash
                notesRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        //following are reference to menu option
        MenuItem sortASC = menu.findItem(R.id.asc_name);
        MenuItem sortDESC = menu.findItem(R.id.desc_name);
        MenuItem sortDateASC = menu.findItem(R.id.asc_date);
        MenuItem sortDateDESC = menu.findItem(R.id.desc_date);

        //following lines call function to sort recycle view from adapter and bond them to menu options
        sortASC.setOnMenuItemClickListener(item -> notesRecyclerAdapter.sortASC());
        sortDESC.setOnMenuItemClickListener(item -> notesRecyclerAdapter.sortDESC());
        sortDateASC.setOnMenuItemClickListener(item -> notesRecyclerAdapter.sortDateASC());
        sortDateDESC.setOnMenuItemClickListener(item -> notesRecyclerAdapter.sortDateDESC());
        return true;
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(NoteViewModel.class);
        noteViewModel.getNotesFromFolderID(folderId).observe(this,notes -> {
            notesRecyclerAdapter = new NotesRecyclerAdapter(this,notes,folderId);
            recyclerView.setAdapter(notesRecyclerAdapter);
            this.setTitle(notes.size()+" - Notes");
        });

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