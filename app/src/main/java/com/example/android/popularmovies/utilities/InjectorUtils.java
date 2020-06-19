package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.PopularMovieDatabase;
import com.example.android.popularmovies.data.network.PopularMovieNetworkDataSource;
import com.example.android.popularmovies.ui.details.MovieDetailsModelFactory;
import com.example.android.popularmovies.ui.main.MainActivityModelFactory;

/*
 * Provides with static methods to inject into various PopularMovie classes
 * */
public class InjectorUtils {
    public static PopularMovieRepository provideRepository(Context context) {
        PopularMovieDatabase repository = PopularMovieDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        PopularMovieNetworkDataSource networkDataSource = provideNetworkDataSource(context);
        return PopularMovieRepository.getInstance(repository.movieDao(), networkDataSource, executors);
    }

    public static PopularMovieNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return PopularMovieNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static MainActivityModelFactory provideMainActivityModelFactory(Context context) {
        return new MainActivityModelFactory(provideRepository(context));
    }

    public static MovieDetailsModelFactory provideMovieDetailsModelFactory(Context context) {
        return new MovieDetailsModelFactory(provideRepository(context));
    }
}
