package com.singh.mynotes.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FolderWithNotes {
    @Embedded
    public Folder folder;
    @Relation(parentColumn = "id", entityColumn = "folderId", entity = Note.class)
    public List<Note> notes;
}
