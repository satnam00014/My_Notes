package com.singh.mynotes.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.singh.mynotes.dao.FolderDao;
import com.singh.mynotes.dao.NoteDao;
import com.singh.mynotes.models.Folder;
import com.singh.mynotes.models.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Folder.class, Note.class}, version = 2, exportSchema = false)
public abstract class NoteAppDatabase extends RoomDatabase {
    private static final String DB_NAME = "note_app_db";
    private static final int NUMBER_OF_THREADS = 4;

    public abstract FolderDao folderDao();
    public abstract NoteDao noteDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static NoteAppDatabase noteAppDatabase;

    public static synchronized NoteAppDatabase getInstance(Context context) {
        if (noteAppDatabase == null) {
                noteAppDatabase = Room.databaseBuilder(context.getApplicationContext(), NoteAppDatabase.class, DB_NAME)
                        .addCallback(rdc)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
        }
        return noteAppDatabase;
    }
    private static final Callback rdc = new Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

            });

        }
    };

}
