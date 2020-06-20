package com.example.android.popularmovies.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.MovieReview;
import com.example.android.popularmovies.data.database.MovieTrailerHolder;

import java.util.List;

public class MovieDetailsViewModel extends ViewModel {
    PopularMovieRepository mRepository;
    LiveData<List<MovieTrailerHolder>> mMovieTrailers;
    LiveData<List<MovieReview>> mMovieReviews;

    public MovieDetailsViewModel(PopularMovieRepository repository) {
        mRepository = repository;
        mMovieTrailers = mRepository.getTrailersLiveData();
        mMovieReviews = mRepository.getReviewsLiveData();
    }

    public LiveData<List<MovieTrailerHolder>> getMovieTrailersLiveData() {
        return mMovieTrailers;
    }

    public LiveData<List<MovieReview>> getMovieReviewsLiveData() {
        return mMovieReviews;
    }

    public void updateTrailersData(int id) {
        mRepository.retrieveTrailersByMovieId(id);
    }

    public void updateReviewsData(int id) {
        mRepository.retrieveReviewsByMovieId(id);
    }
}
