package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable {
    private String title;
    private String posterUrl;
    private String overview;
    private int userRating;
    private String releaseDate;

    public Movie(String title, String posterUrl, String overview, int userRating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
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

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", posterImage=" + posterUrl +
                ", overview='" + overview + '\'' +
                ", userRating=" + userRating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public Movie(Parcel in) {
        this.userRating = in.readInt();
        String[] fieldsArray = new String[4];
        in.readStringArray(fieldsArray);
        this.title = fieldsArray[0];
        this.posterUrl = fieldsArray[1];
        this.overview = fieldsArray[2];
        this.releaseDate = fieldsArray[3];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{title, posterUrl, overview, releaseDate});
        dest.writeInt(userRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
