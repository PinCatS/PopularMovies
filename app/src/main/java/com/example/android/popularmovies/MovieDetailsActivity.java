package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Enable UP button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve the movie info passed from the main activity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Movie.EXTRA_MOVIE_PARCELABLE)) {
                mMovie = intent.getParcelableExtra(Movie.EXTRA_MOVIE_PARCELABLE);
            }
        }

        if (mMovie != null)
            populateUI();
    }

    private Intent createShareMovieIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mMovie.toString())
                .getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareMovieIntent());
        return true;
    }

    private void populateUI() {
        ImageView imageView = findViewById(R.id.iv_poster_details);
        Picasso.get().load(mMovie.getPosterImage()).into(imageView);

        TextView textView = findViewById(R.id.tv_movie_title_details);
        textView.setText(mMovie.getTitle());

        textView = findViewById(R.id.tv_movie_year_details);
        String releaseDate = mMovie.getReleaseDate();
        textView.setText(releaseDate.split("-")[0]); // show only the year for now

        textView = findViewById(R.id.tv_movie_rating_details);
        float rating = mMovie.getUserRating();
        textView.setText(getString(R.string.movie_rating_string, rating));

        textView = findViewById(R.id.tv_movie_overview_details);
        textView.setText(mMovie.getOverview());
    }
}
