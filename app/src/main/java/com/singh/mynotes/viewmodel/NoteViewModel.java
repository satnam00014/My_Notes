package com.singh.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.singh.mynotes.dao.NoteRepository;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.models.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final LiveData<List<Note>> allNotes;
    private final NoteRepository noteRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getNotesFromFolderID(int id) {
        return noteRepository.getNotesFromFolderID(id);
    }

    public LiveData<FolderWithNotes> getNotesWithFolder(int folderId){
        return noteRepository.getNotesWithFolder(folderId);
    }

    public Note getNote(int noteId){
        return noteRepository.getNote(noteId);
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }
}
