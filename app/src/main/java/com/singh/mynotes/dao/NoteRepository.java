package com.singh.mynotes.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.singh.mynotes.db.NoteAppDatabase;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.models.Note;

import java.io.File;
import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;

    public NoteRepository(Application application) {
        NoteAppDatabase db = NoteAppDatabase.getInstance(application);
        noteDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    public LiveData<List<Note>> getNotesFromFolderID(int folderId) {
        return noteDao.getNotes(folderId);
    }

    public List<Note> getNoteList(int folderId){
        return noteDao.getNoteList(folderId);
    }

    public LiveData<FolderWithNotes> getNotesWithFolder(int folderId){
        return noteDao.getNotesWithFolder(folderId);
    }

    public Note getNote(int noteId){
        return noteDao.getNote(noteId);
    }

    public void insert(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> noteDao.insertNote(note));
    }

    public void update(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> noteDao.updateNote(note));
    }

    public void delete(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() ->{
            //write logic to delete image and voice data using path
            if (note.getImagePath()!=null){
                File file = new File(note.getImagePath());
                if (file.exists())
                    file.delete();
            }
            noteDao.deleteNote(note);
        });
    }
}
