package com.example.android.popularmovies.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.NetworkUtilities;

import java.util.List;

/**
 * {@link MainActivityViewModel} class responsible for persisting MainActivity data using {@link LiveData}
 **/
public class MainActivityViewModel extends ViewModel {
    private final PopularMovieRepository mRepository;
    private final LiveData<List<MovieEntry>> mMovies;

    public MainActivityViewModel(PopularMovieRepository repository) {
        mRepository = repository;

        // Get movies live data from repository
        // First call invokes network operation
        mMovies = mRepository.getMoviesLiveData(NetworkUtilities.POPULAR_ENDPOINT);
    }

    public LiveData<List<MovieEntry>> getMoviesLiveData() {
        return mMovies;
    }

    // Invokes network operation
    public void updateMovieData(String endpoint) {
        mRepository.retrieveMoviesFrom(endpoint);
    }
}
