package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

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
        textView.setText(releaseDate.split("-")[0]);

        textView = findViewById(R.id.tv_movie_rating_details);
        float rating = mMovie.getUserRating();
        Log.d("MovieDetailsActivity", "Rating" + rating);
        textView.setTextLocale(Locale.US);
        textView.setText(getString(R.string.movie_rating_string, rating));

        textView = findViewById(R.id.tv_movie_overview_details);
        textView.setText(mMovie.getOverview());
    }
}
