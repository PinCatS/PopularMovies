package com.example.android.popularmovies.ui.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.data.PopularMovieRepository;

public class MovieDetailsModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PopularMovieRepository mRepository;

    public MovieDetailsModelFactory(PopularMovieRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(mRepository);
    }
}
