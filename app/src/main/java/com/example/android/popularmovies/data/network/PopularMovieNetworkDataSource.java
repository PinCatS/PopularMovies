package com.example.android.popularmovies.data.network;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieTrailerHolder;
import com.example.android.popularmovies.utilities.JsonParser;
import com.example.android.popularmovies.utilities.MovieJsonParser;
import com.example.android.popularmovies.utilities.TrailerJsonParser;

import java.net.URL;
import java.util.List;

public class PopularMovieNetworkDataSource {
    private static final String TAG = PopularMovieNetworkDataSource.class.getSimpleName();

    // Singleton instantiation
    private static final Object LOCK = new Object();
    private static PopularMovieNetworkDataSource sInstance;
    private final AppExecutors mExecutors;
    private MutableLiveData<List<MovieEntry>> mDownloadedMovies;
    private MutableLiveData<List<MovieTrailerHolder>> mDownloadedTrailers;

    private static boolean mInitialized;

    private PopularMovieNetworkDataSource(Context context, AppExecutors executors) {
        mExecutors = executors;
        mDownloadedMovies = new MutableLiveData<>();
        mDownloadedTrailers = new MutableLiveData<>();
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

    public LiveData<List<MovieTrailerHolder>> getTrailersLiveData() {
        return mDownloadedTrailers;
    }

    public void retrieveTrailersByMovieId(int id) {
        String endpoint = NetworkUtilities.MOVIE_TRAILERS_ENDPOINT;
        endpoint = String.format(endpoint, id);
        fetchTrailers(endpoint);
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
        JsonParser movieJsonParser = new MovieJsonParser();

        Log.d(TAG, "Start downloading new trailers data from " + endpoint);
        mExecutors.networkIO().execute(() -> {
            try {

                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                List<MovieEntry> movieEntries = (List<MovieEntry>) movieJsonParser.parseJson(jsonResponse);
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

    /*
     * Get the trailers data for a movie and notifies subscribers of movie trailer live data
     * */
    public void fetchTrailers(String endpoint) {

        URL trailersUrl = NetworkUtilities.buildURL(endpoint);
        Log.d(TAG, "Movie trailers url: " + trailersUrl);
        JsonParser trailerJsonParser = new TrailerJsonParser();

        Log.d(TAG, "Start downloading new trailers data from " + endpoint);
        mExecutors.networkIO().execute(() -> {
            try {

                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(trailersUrl);
                List<MovieTrailerHolder> movieTrailerEntries = (List<MovieTrailerHolder>) trailerJsonParser.parseJson(jsonResponse);
                Log.d(TAG, "JSON parsing finished");

                if (movieTrailerEntries.size() > 0) {
                    Log.d(TAG, "Trailers entries have " + movieTrailerEntries.size() + " values");
                    Log.d(TAG, "The first trailer is " + movieTrailerEntries.get(0));

                    // trigger call to observers
                    mDownloadedTrailers.postValue(movieTrailerEntries);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
