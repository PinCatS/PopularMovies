package com.example.android.popularmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * {@link Dao} provides an API to accomplish operations against {@link PopularMovieDatabase}
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(final int id);

    @Query("SELECT EXISTS(SELECT * FROM movie WHERE id = :id)")
    boolean hasMovie(int id);

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(MovieEntry... movieEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void deleteMovie(MovieEntry movieEntry);
}
