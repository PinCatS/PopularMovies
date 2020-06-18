package com.example.android.popularmovies.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * {@link PopularMovieDatabase} database that contain movie table for {@link MovieEntry}
 * with the {@link MovieDao} dao
 */
@Database(entities = {MovieEntry.class, MovieTrailer.class}, version = 1, exportSchema = false)
public abstract class PopularMovieDatabase extends RoomDatabase {
    private static final String TAG = PopularMovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovie";
    private static PopularMovieDatabase sInstance;

    public static PopularMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new PopularMovie database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PopularMovieDatabase.class, PopularMovieDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the PopularMovie database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();

    public abstract MovieTrailerDao movieTrailerDao();
}
