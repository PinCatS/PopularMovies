package com.example.android.popularmovies.utilities;

import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY_QUERY = "api_key";
    public static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    public static final String POPULAR_ENDPOINT = "movie/popular";

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

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }
}
