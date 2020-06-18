package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.AppExecutors;
import com.example.android.popularmovies.data.network.PopularMovieNetworkDataSource;
import com.example.android.popularmovies.ui.main.MainActivityModelFactory;

/*
 * Provides with static methods to inject into various PopularMovie classes
 * */
public class InjectorUtils {

    public static PopularMovieNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return PopularMovieNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static MainActivityModelFactory provideMainActivityModelFactory(Context context) {
        return new MainActivityModelFactory(context);
    }
}
