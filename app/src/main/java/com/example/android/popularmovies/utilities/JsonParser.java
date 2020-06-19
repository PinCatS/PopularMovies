package com.example.android.popularmovies.utilities;

import org.json.JSONException;

public abstract class JsonParser {
    protected static final String KEY_RESULTS = "results";

    public abstract Object parseJson(String jsonString) throws JSONException;
}
