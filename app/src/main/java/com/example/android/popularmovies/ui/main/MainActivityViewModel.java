package com.example.android.popularmovies.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;

import java.util.List;

/**
 * {@link MainActivityViewModel} class responsible for persisting MainActivity data using {@link LiveData}
 **/
public class MainActivityViewModel extends ViewModel {
    public static final String TOP_RATED_VIEW = "top_rated_view";
    public static final String POPULAR_VIEW = "popular_view";
    public static final String FAVORITES_VIEW = "favorite_view";
    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private final PopularMovieRepository mRepository;
    private final LiveData<List<MovieEntry>> mMovies;
    private final LiveData<List<MovieEntry>> mFavoriteMovies;

    private String viewType = POPULAR_VIEW;

    public MainActivityViewModel(PopularMovieRepository repository) {
        Log.d(TAG, "New MainActivityViewModel created");

        mRepository = repository;

        // Get movies live data from repository
        // First call invokes network operation
        mMovies = mRepository.getMoviesLiveData(PopularMovieRepository.POPULAR_ENDPOINT);
        mFavoriteMovies = mRepository.getMovieDao().loadAllMovies();
    }

    public LiveData<List<MovieEntry>> getMoviesLiveData(String viewType) {
        if (viewType != null && viewType.equals(FAVORITES_VIEW)) {
            return mFavoriteMovies;
        } else {
            return mMovies;
        }
    }

    public LiveData<Boolean> getMoviesFetchFailureLiveData() {
        return mRepository.getFetchFailureLiveData();
    }

    // Invokes network operation
    public void updateMovieData() {
        String endpoint;

        switch (viewType) {
            default:
            case POPULAR_VIEW:
                endpoint = PopularMovieRepository.POPULAR_ENDPOINT;
                break;
            case TOP_RATED_VIEW:
                endpoint = PopularMovieRepository.TOP_RATED_ENDPOINT;
                break;
            case FAVORITES_VIEW:
                endpoint = PopularMovieRepository.FAVORITES_ENDPOINT;
                break;
        }

        mRepository.retrieveMoviesFrom(endpoint);
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}
