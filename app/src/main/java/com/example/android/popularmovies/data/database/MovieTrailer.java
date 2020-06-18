package com.example.android.popularmovies.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "trailer", foreignKeys = @ForeignKey(entity = MovieEntry.class,
        parentColumns = "id",
        childColumns = "movieId",
        onDelete = CASCADE))
public class MovieTrailer implements Parcelable {

    @NonNull
    @PrimaryKey
    final private String key;
    private String name;
    private int movieId;

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public MovieTrailer(@NonNull String key, String name, int movieId) {
        this.key = key;
        this.name = name;
        this.movieId = movieId;
    }

    @Ignore
    private MovieTrailer(Parcel in) {
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
