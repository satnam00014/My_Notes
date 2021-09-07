package com.singh.mynotes.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "folderId")
    private int folderId;

    @NonNull
    @ColumnInfo(name = "noteTitle")
    private String noteTitle;

    @NonNull
    @ColumnInfo(name = "noteSubject")
    private String noteSubject;

    @NonNull
    @ColumnInfo(name = "noteContent")
    private String noteContent;

    @NonNull
    @ColumnInfo(name = "noteDate")
    private String noteDate;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    public Note(int folderId, String noteTitle,String noteSubject, String noteContent, String noteDate) {
        this.folderId = folderId;
        this.noteTitle = noteTitle;
        this.noteSubject = noteSubject;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setFolderId(int id) {this.folderId = id;}

    public int getFolderId() {
        return folderId;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public void setNoteTitle(String content) {
        this.noteTitle = content;
    }

    public String getNoteSubject() { return noteSubject;}

    public void setNoteSubject( String noteSubject) { this.noteSubject = noteSubject; }

    public String getNoteContent() {
        return this.noteContent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public String getNoteDate() {
        return this.noteDate;
    }

    public void setNoteDate(String date) {
        this.noteDate = date;
    }
}
