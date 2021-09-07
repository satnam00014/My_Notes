package com.singh.mynotes.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.singh.mynotes.db.NoteAppDatabase;
import com.singh.mynotes.models.Folder;
import com.singh.mynotes.models.FolderWithNotes;
import com.singh.mynotes.models.Note;

import java.util.List;

public class FolderRepository {
    private final FolderDao folderDao;
    private final Application application;
    public FolderRepository(Application application) {
        NoteAppDatabase db = NoteAppDatabase.getInstance(application);
        this.application = application;
        folderDao = db.folderDao();
    }

    public int getFolderNotesCount(int id) {
        return folderDao.getFolderNotesCount(id);
    }

    public LiveData<List<FolderWithNotes>> getAllFolderWithNotes() {
        return folderDao.loadFolderList();
    }


    public LiveData<List<Folder>> folderListExcept(int folderId){
        return folderDao.folderListExcept(folderId);
    }

    public void insert(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> folderDao.insertFolder(folder));
    }

    public void update(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> folderDao.updateFolder(folder));
    }

    public void delete(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> {
            NoteRepository noteRepository = new NoteRepository(application);
            List<Note> notes = noteRepository.getNoteList(folder.getId());
            for (Note note : notes){
                noteRepository.delete(note);
            }
            folderDao.deleteFolder(folder);
        });
    }
}
