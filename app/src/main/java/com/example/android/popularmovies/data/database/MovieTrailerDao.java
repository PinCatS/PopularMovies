package com.example.android.popularmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieTrailerDao {
    @Query("SELECT * FROM trailer WHERE movie_id = :movieId")
    LiveData<List<MovieTrailerEntry>> findTrailersForMovie(final int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(MovieTrailerEntry... trailers);

    @Delete
    void deleteTrailers(MovieTrailerEntry... trailers);
}
