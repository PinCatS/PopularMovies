package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.BuildConfig;

import java.net.URL;

public final class NetworkUtilities {
    private static final String THE_MOVIEDB_BASE_PATH = "http://api.themoviedb.org/3";
    private static final String THEMOVIEDB_API_KEY = BuildConfig.THEMOVIEDB_API_KEY;

    public static URL buildURL(String endpoint) {
        URL url = null;
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) {
        return null;
    }
}
