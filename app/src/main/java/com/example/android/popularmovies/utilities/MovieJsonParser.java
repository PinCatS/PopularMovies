package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.database.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieJsonParser extends JsonParser {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185"; // recommended

    private static final String KEY_MOVIE_ID = "id";
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
        /* Not sure at the moment if simple String concatenation will be more suitable here.
         *  Since it will be called for every parsed movie, I decided to use the builder due to
         *  performance. But might be it is worth to check if the performance is really different
         *  here. */
        StringBuilder builder = new StringBuilder(baseUrl);
        return builder.append(size).append(relativePath).toString();
    }

    @Override
    public ArrayList<MovieEntry> parseJson(String jsonString) throws JSONException {

        if (jsonString == null) throw new JSONException("Input JSON string is null");

        ArrayList<MovieEntry> movieEntryList = new ArrayList<>();
        JSONObject movieJson = new JSONObject(jsonString);

        JSONArray resultsJsonArray = movieJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsJsonArray.length(); ++i) {
            JSONObject movieJsonItem = resultsJsonArray.getJSONObject(i);
            int id = movieJsonItem.getInt(KEY_MOVIE_ID);
            String title = movieJsonItem.getString(KEY_TITLE);
            String posterUrl =
                    buildPosterUrl(POSTER_BASE_URL, POSTER_SIZE, movieJsonItem.getString(KEY_POSTER_PATH));
            String overview = movieJsonItem.getString(KEY_OVERVIEW);
            int rating = movieJsonItem.getInt(KEY_RATING);

            String releaseDate = null;
            if (movieJsonItem.has(KEY_RELEASE_DATE)) {
                releaseDate = movieJsonItem.getString(KEY_RELEASE_DATE);
            }

            movieEntryList.add(new MovieEntry(id, title, posterUrl, overview, rating, releaseDate));
        }

        return movieEntryList;
    }
}
