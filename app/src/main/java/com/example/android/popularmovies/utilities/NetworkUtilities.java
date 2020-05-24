package com.example.android.popularmovies.utilities;

import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final String THEMOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    private static final String API_KEY_QUERY = "api_key";

    public static URL buildURL(String endpoint) {
        Uri uri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(endpoint)
                .appendQueryParameter(API_KEY_QUERY, BuildConfig.THEMOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) {
        return null;
    }
}
