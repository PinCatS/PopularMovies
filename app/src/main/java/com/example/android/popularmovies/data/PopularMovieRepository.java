package com.example.android.popularmovies.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.database.MovieDao;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.PopularMovieNetworkDataSource;

import java.util.List;

public class PopularMovieRepository {
    private static final String TAG = PopularMovieRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static PopularMovieRepository sInstance;
    private static boolean mInitialized;

    private final MovieDao mMovieDao;
    private final PopularMovieNetworkDataSource mMovieNetworkDataSource;
    private final AppExecutors mExecutors;

    private PopularMovieRepository(MovieDao dao, PopularMovieNetworkDataSource movieNetworkDataSource,
                                   AppExecutors executors) {

        mMovieDao = dao;
        mMovieNetworkDataSource = movieNetworkDataSource;
        mExecutors = executors;
    }

    public synchronized static PopularMovieRepository getInstance(MovieDao dao, PopularMovieNetworkDataSource movieNetworkDataSource,
                                                                  AppExecutors executors) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PopularMovieRepository(dao, movieNetworkDataSource, executors);
            }
            Log.d(TAG, "Created new repository");
        }
        return sInstance;
    }

    /*
     * Gets movie live data.
     * First call invokes retrieval of movies from endpoint
     * */
    public LiveData<List<MovieEntry>> getMoviesLiveData(String endpoint) {
        return mMovieNetworkDataSource.getMoviesLiveData(endpoint);
    }

    /*
     * Invokes retrieval of movies from endpoint
     * */
    public void retrieveMoviesFrom(String endpoint) {
        mMovieNetworkDataSource.retrieveMoviesFrom(endpoint);
    }
}
