package com.example.android.popularmovies.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.data.PopularMovieRepository;

public class MainActivityModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PopularMovieRepository mRepository;

    public MainActivityModelFactory(PopularMovieRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}
