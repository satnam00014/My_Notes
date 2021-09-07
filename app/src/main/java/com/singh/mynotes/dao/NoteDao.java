package com.singh.mynotes.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE folderId=:id")
    LiveData<List<Note>> getNotes(int id);

    @Query("SELECT * FROM notes WHERE folderId=:id")
    List<Note> getNoteList(int id);

    @Transaction
    @Query("SELECT * FROM folders WHERE id = :folderId")
    LiveData<FolderWithNotes> getNotesWithFolder(int folderId);

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNote(int id);
}
