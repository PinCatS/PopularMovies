package com.example.android.popularmovies.data.database;

public class MovieReview {
    private final String id;
    private final String name;
    private final String content;
    private final String url;
    private int movieId;

    public MovieReview(String id, String name, String content, String url) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "MovieReview{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
