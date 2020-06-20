package com.example.android.popularmovies.ui.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.data.PopularMovieRepository;

public class MovieDetailsModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PopularMovieRepository mRepository;
    private final int mMovieId;

    public MovieDetailsModelFactory(PopularMovieRepository repository, int movieId) {
        mRepository = repository;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mRepository, mMovieId);
    }
}
