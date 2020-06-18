package com.example.android.popularmovies.data.database;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieEntry implements Parcelable {

    private int id;
    private String title;
    private String posterUrl;
    private String overview;
    private float userRating;
    private String releaseDate;

    public static final Parcelable.Creator<MovieEntry> CREATOR = new Parcelable.Creator<MovieEntry>() {
        public MovieEntry createFromParcel(Parcel in) {
            return new MovieEntry(in);
        }

        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };

    public MovieEntry(int id, String title, String posterUrl, String overview, int userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

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

    public String getPosterImage() {
        return posterUrl;
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
