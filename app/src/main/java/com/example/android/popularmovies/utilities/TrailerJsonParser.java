package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.database.MovieTrailerHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrailerJsonParser extends JsonParser {
    private static final String KEY_TRAILER_KEY = "key";
    private static final String KEY_TRAILER_NAME = "name";
    private static final String KEY_VIDEO_TYPE = "type";

    @Override
    public ArrayList<MovieTrailerHolder> parseJson(String jsonString) throws JSONException {

        if (jsonString == null) throw new JSONException("Input JSON string is null");

        ArrayList<MovieTrailerHolder> trailersList = new ArrayList<>();
        JSONObject trailerJson = new JSONObject(jsonString);

        JSONArray resultsJsonArray = trailerJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsJsonArray.length(); ++i) {
            JSONObject trailerJsonItem = resultsJsonArray.getJSONObject(i);
            if (trailerJsonItem.getString(KEY_VIDEO_TYPE).equals("Trailer")) {
                String key = trailerJsonItem.getString(KEY_TRAILER_KEY);
                String name = trailerJsonItem.getString(KEY_TRAILER_NAME);

                trailersList.add(new MovieTrailerHolder(key, name));
            }
        }

        return trailersList;
    }
}
