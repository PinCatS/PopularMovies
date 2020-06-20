package com.example.android.popularmovies.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "trailer", foreignKeys = @ForeignKey(entity = MovieEntry.class,
        parentColumns = "id",
        childColumns = "movie_id",
        onDelete = CASCADE))
public class MovieTrailerEntry implements Parcelable {
    @Ignore
    public static final String TRAILER_YOUTUBE_BASE_URL = "https://www.youtube.com/";

    @NonNull
    @PrimaryKey
    final private String key;
    private String name;
    public static final Parcelable.Creator<MovieTrailerEntry> CREATOR = new Parcelable.Creator<MovieTrailerEntry>() {
        public MovieTrailerEntry createFromParcel(Parcel in) {
            return new MovieTrailerEntry(in);
        }

        public MovieTrailerEntry[] newArray(int size) {
            return new MovieTrailerEntry[size];
        }
    };
    @ColumnInfo(name = "movie_id")
    private int movieId;

    public MovieTrailerEntry(@NonNull String key, String name, int movieId) {
        this.key = key;
        this.name = name;
        this.movieId = movieId;
    }

    @Ignore
    public MovieTrailerEntry(int movieId, MovieTrailerHolder holder) {
        this.key = holder.getKey();
        this.name = holder.getName();
        this.movieId = movieId;
    }

    @Ignore
    private MovieTrailerEntry(Parcel in) {
        key = in.readString();
        name = in.readString();
        movieId = in.readInt();
    }

    public String getName() {
        return name;
    }

    public int getMovieId() {
        return movieId;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeInt(movieId);
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
