package com.example.android.popularmovies.data.database;

import androidx.annotation.NonNull;

/*
 * Trailer class to hold data from network
 * Finally will be merged with MovieTrailerEntry
 * Used because at the time of parsing network data and building Trailer class
 * we don't know the movie id and it is required because sued as a foreign key in MovieTrailerEntry
 * */
public class MovieTrailerHolder {
    final private String key;
    private String name;

    public MovieTrailerHolder(@NonNull String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MovieTrailerHolder{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
