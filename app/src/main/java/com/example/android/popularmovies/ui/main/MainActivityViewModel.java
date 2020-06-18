package com.example.android.popularmovies.ui.main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.PopularMovieNetworkDataSource;
import com.example.android.popularmovies.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link MainActivityViewModel} class responsible for persisting MainActivity data using {@link LiveData}
 **/
public class MainActivityViewModel extends ViewModel {
    private final LiveData<List<MovieEntry>> mMovies;

    public MainActivityViewModel(Context context) {
        PopularMovieNetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(context);
        mMovies = networkDataSource.getCurrentMovies();
    }

    public LiveData<List<MovieEntry>> getMovies() {
        return mMovies;
    }
}
