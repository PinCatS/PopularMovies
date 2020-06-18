package com.example.android.popularmovies.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Defines a schema of a table in {@link androidx.room.Room} for a single Movie entry
 */
@Entity(tableName = "movie")
public class MovieEntry implements Parcelable {

    public static final Parcelable.Creator<MovieEntry> CREATOR = new Parcelable.Creator<MovieEntry>() {
        public MovieEntry createFromParcel(Parcel in) {
            return new MovieEntry(in);
        }

        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };
    private String title;
    private String overview;
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "poster_url")
    private String posterUrl;
    @ColumnInfo(name = "user_rating")
    private float userRating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public MovieEntry(int id, String title, String posterUrl, String overview, float userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    @Ignore
    private MovieEntry(Parcel in) {
        String[] fieldsArray = new String[4];
        in.readStringArray(fieldsArray);
        this.title = fieldsArray[0];
        this.posterUrl = fieldsArray[1];
        this.overview = fieldsArray[2];
        this.releaseDate = fieldsArray[3];
        this.userRating = in.readFloat();
        this.id = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public float getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return title + "\n\n" +
                "Release date: " + releaseDate + "\n" +
                "Rating      : " + userRating + "/10\n" +
                "Overview    : " + overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{title, posterUrl, overview, releaseDate});
        dest.writeFloat(userRating);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
