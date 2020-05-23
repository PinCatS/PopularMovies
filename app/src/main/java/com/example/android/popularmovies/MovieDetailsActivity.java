package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Movie.EXTRA_MOVIE_PARCELABLE)) {
                mMovie = intent.getParcelableExtra(Movie.EXTRA_MOVIE_PARCELABLE);
            }
        }

        Log.d("MovieDetailsActivity", "Movie: " + mMovie);
    }
}
