package com.singh.mynotes.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.singh.mynotes.models.Folder;
import com.singh.mynotes.models.FolderWithNotes;

import java.util.List;

@Dao
public interface FolderDao {
    @Transaction
    @Query("SELECT * FROM folders")
    LiveData<List<FolderWithNotes>> loadFolderList();

    @Query("SELECT COUNT(folderId) FROM notes WHERE folderId=:id")
    int getFolderNotesCount(int id);

    @Query("SELECT * FROM folders WHERE id NOT IN (:folderId)")
    LiveData<List<Folder>> folderListExcept(int folderId);

    @Insert
    void insertFolder(Folder folder);

    @Update
    void updateFolder(Folder folder);

    @Delete
    void deleteFolder(Folder folder);
}
