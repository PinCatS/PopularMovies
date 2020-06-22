package com.example.android.popularmovies.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

public class MovieTrailer implements Parcelable {
    public static final String TRAILER_YOUTUBE_BASE_URL = "https://www.youtube.com/";

    final private String key;
    private String name;

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public MovieTrailer(@NonNull String key, String name) {
        this.key = key;
        this.name = name;
    }

    @Ignore
    private MovieTrailer(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(key);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "MovieTrailerEntry{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
