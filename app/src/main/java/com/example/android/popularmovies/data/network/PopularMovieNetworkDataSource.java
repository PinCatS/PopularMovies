package com.example.android.popularmovies.data.network;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.utilities.MovieJasonUtils;

import java.net.URL;
import java.util.List;

public class PopularMovieNetworkDataSource {
    private static final String TAG = PopularMovieNetworkDataSource.class.getSimpleName();

    // Singleton instantiation
    private static final Object LOCK = new Object();
    private static PopularMovieNetworkDataSource sInstance;
    private final AppExecutors mExecutors;
    private MutableLiveData<List<MovieEntry>> mDownloadedMovies;

    private static boolean mInitialized;

    private PopularMovieNetworkDataSource(Context context, AppExecutors executors) {
        mExecutors = executors;
        mDownloadedMovies = new MutableLiveData<>();
    }

    public static PopularMovieNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(TAG, "Getting the PopularMovie network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PopularMovieNetworkDataSource(context, executors);
                Log.d(TAG, "Made new PopularMovie network data source");
            }
        }
        return sInstance;
    }

    public LiveData<List<MovieEntry>> getMoviesLiveData(String endpoint) {
        initializeData(endpoint);
        return mDownloadedMovies;
    }

    public void retrieveMoviesFrom(String endpoint) {
        fetchMovies(endpoint);
    }

    private void initializeData(String endpoint) {
        if (mInitialized) return;
        mInitialized = true;
        fetchMovies(endpoint);
    }

    /*
     * Get the newest movie data from network endpoint and notifies subscribers of movie live data
     * */
    public void fetchMovies(String endpoint) {

        URL movieUrl = NetworkUtilities.buildURL(endpoint);

        Log.d(TAG, "Start downloading new movies data from " + endpoint);
        mExecutors.networkIO().execute(() -> {
            try {

                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                List<MovieEntry> movieEntries = MovieJasonUtils.createFromJsonString(jsonResponse);
                Log.d(TAG, "JSON parsing finished");

                if (movieEntries.size() > 0) {
                    Log.d(TAG, "Movie entries have " + movieEntries.size() + " values");
                    Log.d(TAG, "The first movie is " + movieEntries.get(0));

                    // trigger call to observers
                    mDownloadedMovies.postValue(movieEntries);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
