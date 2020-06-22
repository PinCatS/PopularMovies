package com.example.android.popularmovies.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.database.MovieDao;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieReview;
import com.example.android.popularmovies.data.database.MovieTrailer;
import com.example.android.popularmovies.data.network.PopularMovieNetworkDataSource;

import java.util.List;

import javax.annotation.Nullable;

public class PopularMovieRepository {
    public static final String TOP_RATED_ENDPOINT = "movie/top_rated";
    public static final String POPULAR_ENDPOINT = "movie/popular";
    public static final String FAVORITES_ENDPOINT = "favorites";


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

    public MovieDao getMovieDao() {
        return mMovieDao;
    }

    public void checkIfMovieInFavorite(MutableLiveData<Boolean> isFavorite, int movieId) {
        mExecutors.diskIO().execute(() -> {
            boolean hasMovie = mMovieDao.hasMovie(movieId);
            isFavorite.postValue(hasMovie);
            Log.d("MovieDetailViewModel", "Is movie in a database? --> " + hasMovie);
        });
    }

    public void saveMovieAsFavorite(MovieEntry movie) {
        mExecutors.diskIO().execute(() -> {
            mMovieDao.insertMovie(movie);
            Log.d(TAG, String.format("Insert movie %s into a database", movie.getTitle()));
        });
    }

    public void removeFromFavorite(MovieEntry movie) {
        mExecutors.diskIO().execute(() -> {
            mMovieDao.deleteMovie(movie);
            Log.d(TAG, String.format("Remove movie %s from a database", movie.getTitle()));
        });
    }

    /*
     * Gets movie live data.
     * First call invokes retrieval of movies from endpoint
     * */
    public LiveData<List<MovieEntry>> getMoviesLiveData(String endpoint) {
        return mMovieNetworkDataSource.getMoviesLiveData(endpoint);
    }

    public LiveData<List<MovieTrailer>> getTrailersLiveData() {
        return mMovieNetworkDataSource.getTrailersLiveData();
    }

    public LiveData<List<MovieReview>> getReviewsLiveData() {
        return mMovieNetworkDataSource.getReviewsLiveData();
    }

    /*
     * Invokes retrieval of movies from endpoint
     * */
    public void retrieveMoviesFrom(@Nullable String endpoint) {
        if (endpoint == null || !endpoint.equals(FAVORITES_ENDPOINT)) {
            mMovieNetworkDataSource.retrieveMoviesFrom(endpoint);
        }
    }

    public void retrieveTrailersByMovieId(int id) {
        mMovieNetworkDataSource.retrieveTrailersByMovieId(id);
    }

    public void retrieveReviewsByMovieId(int id) {
        mMovieNetworkDataSource.retrieveReviewsByMovieId(id);
    }
}
