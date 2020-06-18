package com.example.android.popularmovies.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;

public class MovieDetailsViewModel extends ViewModel {
    PopularMovieRepository mRepository;
    LiveData<MovieEntry> mMovieEntry;

    public MovieDetailsViewModel(PopularMovieRepository repository) {
        mRepository = repository;
    }
}
