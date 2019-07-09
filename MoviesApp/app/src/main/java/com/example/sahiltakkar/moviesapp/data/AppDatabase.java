package com.example.sahiltakkar.moviesapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;


@Database(entities = {MovieEntry.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG=AppDatabase.class.getSimpleName();
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="moviesList";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.v(LOG_TAG, "creating new database Instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries().build();
            }
        }

        Log.v(LOG_TAG,"getting database instance");
        return sInstance;

    }


    public abstract MovieDAO movieDAO();

}
