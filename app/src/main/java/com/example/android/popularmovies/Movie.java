package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final String EXTRA_MOVIE_PARCELABLE = "Movie";

    private String title;
    private String posterUrl;
    private String overview;
    private float userRating;
    private String releaseDate;

    public Movie(String title, String posterUrl, String overview, int userRating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        String[] fieldsArray = new String[4];
        in.readStringArray(fieldsArray);
        this.title = fieldsArray[0];
        this.posterUrl = fieldsArray[1];
        this.overview = fieldsArray[2];
        this.releaseDate = fieldsArray[3];
        this.userRating = in.readFloat();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
