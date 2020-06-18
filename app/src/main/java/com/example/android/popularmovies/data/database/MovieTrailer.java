package com.example.android.popularmovies.data.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "trailer", foreignKeys = @ForeignKey(entity = MovieEntry.class,
        parentColumns = "id",
        childColumns = "movieId",
        onDelete = CASCADE))
public class MovieTrailer {

    @NonNull
    @PrimaryKey
    final private String key;
    private String name;
    private int movieId;

    public MovieTrailer(String key, String name, int movieId) {
        this.key = key;
        this.name = name;
        this.movieId = movieId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "MovieTrailer{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
