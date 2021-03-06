package com.example.android.popularmovies.data.network;

import android.net.Uri;
import android.util.Log;

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
    public static final String MOVIE_TRAILERS_ENDPOINT = "movie/%d/videos";
    public static final String MOVIE_REVIEWS_ENDPOINT = "movie/%d/reviews";

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

        Log.d(TAG, "Url:" + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String jsonString = null;

        if (url == null) {
            return null;
        }

        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                jsonString = readFromInputStream(is);
            } else {
                Log.e(TAG, "Bad response from the server: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (is != null) {
                is.close();
            }
        }

        return jsonString;
    }

    private static String readFromInputStream(InputStream is) {
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
}
