package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.database.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewJsonParser extends JsonParser {
    private static final String KEY_REVIEW_ID = "id";
    private static final String KEY_REVIEW_CONTENT = "content";
    private static final String KEY_REVIEW_AUTHOR = "author";
    private static final String KEY_REVIEW_URL = "url";

    @Override
    public ArrayList<MovieReview> parseJson(String jsonString) throws JSONException {

        if (jsonString == null) throw new JSONException("Input JSON string is null");

        ArrayList<MovieReview> reviewsList = new ArrayList<>();
        JSONObject reviewJson = new JSONObject(jsonString);

        JSONArray resultsJsonArray = reviewJson.getJSONArray(KEY_RESULTS);
        for (int i = 0; i < resultsJsonArray.length(); ++i) {
            JSONObject reviewJsonItem = resultsJsonArray.getJSONObject(i);

            String id = reviewJsonItem.getString(KEY_REVIEW_ID);
            String authorName = reviewJsonItem.getString(KEY_REVIEW_AUTHOR);
            String content = reviewJsonItem.getString(KEY_REVIEW_CONTENT);
            String url = reviewJsonItem.getString(KEY_REVIEW_URL);

            reviewsList.add(new MovieReview(id, authorName, content, url));
        }

        return reviewsList;
    }
}
