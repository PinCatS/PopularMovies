package com.example.android.popularmovies;

class Movie {
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
}
