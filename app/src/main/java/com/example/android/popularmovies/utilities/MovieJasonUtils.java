package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieJasonUtils {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w500";

    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "original_title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RATING = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    /**
     * Constructs poster url from base url, size and relative path.
     *
     * @param baseUrl      taken from themoviedb doc
     * @param size         poster size that can be "w92", "w154", "w185", "w342", "w500", "w780", or "original"
     * @param relativePath returned by the moviedb api
     * @return String of poster url
     */
    private static String buildPosterUrl(String baseUrl, String size, String relativePath) {
        StringBuilder builder = new StringBuilder(baseUrl);
        return builder.append(size).append(relativePath).toString();
    }

    public static List<Movie> createFromJsonString(String movieJsonString) throws JSONException {
        List<Movie> movieList = new ArrayList<>();
        JSONObject movieJson = new JSONObject(movieJsonString);

        JSONArray resultsJsonArray = movieJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsJsonArray.length(); ++i) {
            JSONObject movieJsonItem = resultsJsonArray.getJSONObject(i);
            String title = movieJsonItem.getString(KEY_TITLE);
            String posterUrl =
                    buildPosterUrl(POSTER_BASE_URL, POSTER_SIZE, movieJsonItem.getString(KEY_POSTER_PATH));
            String overview = movieJsonItem.getString(KEY_OVERVIEW);
            int rating = movieJsonItem.getInt(KEY_RATING);
            String releaseDate = movieJsonItem.getString(KEY_RELEASE_DATE);

            movieList.add(new Movie(title, posterUrl, overview, rating, releaseDate));
        }

        return movieList;
    }


}
