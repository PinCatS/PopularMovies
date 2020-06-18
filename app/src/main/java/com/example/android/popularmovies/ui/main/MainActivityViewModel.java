package com.example.android.popularmovies.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.database.MovieEntry;

import java.util.List;

/**
 * {@link MainActivityViewModel} class responsible for persisting MainActivity data using {@link LiveData}
 **/
public class MainActivityViewModel extends ViewModel {
    private final LiveData<List<MovieEntry>> mMovies;

    public MainActivityViewModel() {
        mMovies = null;
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return mMovies;
    }
}
