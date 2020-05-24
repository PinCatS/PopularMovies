package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

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

        TextView textView = findViewById(R.id.tv_movie_title_details);
        String title = mMovie.getTitle();
        textView.setText(title);

        if (mMovie != null)
            populateUI();
    }

    private void populateUI() {
        ImageView imageView = findViewById(R.id.iv_poster_details);
        Picasso.get().load(mMovie.getPosterImage()).into(imageView);

        TextView textView = findViewById(R.id.tv_movie_title_details);
        textView.setText(mMovie.getTitle());

        textView = findViewById(R.id.tv_movie_year_details);
        String releaseDate = mMovie.getReleaseDate();
        textView.setText(releaseDate.split("-")[0]);

        textView = findViewById(R.id.tv_movie_rating_details);
        textView.setText(Double.toString(mMovie.getUserRating()));

        textView = findViewById(R.id.tv_movie_overview_details);
        textView.setText(mMovie.getOverview());
    }
}
