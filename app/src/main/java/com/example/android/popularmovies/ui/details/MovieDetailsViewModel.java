package com.example.android.popularmovies.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.PopularMovieRepository;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieReview;
import com.example.android.popularmovies.data.database.MovieTrailer;

import java.util.List;

public class MovieDetailsViewModel extends ViewModel {
    PopularMovieRepository mRepository;
    LiveData<List<MovieTrailer>> mMovieTrailers;
    LiveData<List<MovieReview>> mMovieReviews;
    MutableLiveData<Boolean> isMovieInFavorite = new MutableLiveData<>();

    public MovieDetailsViewModel(PopularMovieRepository repository, int movieId) {
        mRepository = repository;
        mMovieTrailers = mRepository.getTrailersLiveData();
        mMovieReviews = mRepository.getReviewsLiveData();

        // Do initial checks and requests
        mRepository.checkIfMovieInFavorite(isMovieInFavorite, movieId);
        updateTrailersData(movieId);
        updateReviewsData(movieId);
    }

    public LiveData<List<MovieTrailer>> getMovieTrailersLiveData() {
        return mMovieTrailers;
    }

    public LiveData<List<MovieReview>> getMovieReviewsLiveData() {
        return mMovieReviews;
    }

    private void updateTrailersData(int id) {
        mRepository.retrieveTrailersByMovieId(id);
    }

    private void updateReviewsData(int id) {
        mRepository.retrieveReviewsByMovieId(id);
    }

    public void saveAsFavorite(MovieEntry movie) {
        mRepository.saveMovieAsFavorite(movie);
        mRepository.checkIfMovieInFavorite(isMovieInFavorite, movie.getId());
        mRepository.retrieveMoviesFrom(PopularMovieRepository.FAVORITES_ENDPOINT);
    }

    public void removeFromFavorite(MovieEntry movie) {
        mRepository.removeFromFavorite(movie);
        mRepository.checkIfMovieInFavorite(isMovieInFavorite, movie.getId());
        mRepository.retrieveMoviesFrom(PopularMovieRepository.FAVORITES_ENDPOINT);
    }

    public LiveData<Boolean> isFavorite() {
        return isMovieInFavorite;
    }
}
