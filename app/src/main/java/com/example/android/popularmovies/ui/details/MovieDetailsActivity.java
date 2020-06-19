package com.example.android.popularmovies.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieTrailerEntry;
import com.example.android.popularmovies.data.database.MovieTrailerHolder;
import com.example.android.popularmovies.utilities.InjectorUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_PARCELABLE = "Movie";

    private MovieEntry mMovieEntry;

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
            if (intent.hasExtra(EXTRA_MOVIE_PARCELABLE)) {
                mMovieEntry = intent.getParcelableExtra(EXTRA_MOVIE_PARCELABLE);
            }
        }

        if (mMovieEntry != null) {
            // Setup ModelView
            MovieDetailsModelFactory modelViewFactory = InjectorUtils.provideMovieDetailsModelFactory(this);
            MovieDetailsViewModel modelView = new ViewModelProvider(this, modelViewFactory).get(MovieDetailsViewModel.class);
            modelView.getMovieTrailersLiveData().observe(this, newTrailerHolders -> {
                int movieId = mMovieEntry.getId();
                List<MovieTrailerEntry> trailers = new ArrayList<>();
                for (MovieTrailerHolder holder : newTrailerHolders) {
                    trailers.add(new MovieTrailerEntry(movieId, holder));
                }
                mMovieEntry.setTrailers(trailers);
                populateUI();
            });

            // Retrieve movie trailers and reviews unless we have them already
            List<MovieTrailerEntry> trailers = mMovieEntry.getTrailers();
            if (trailers == null || trailers.size() == 0) {
                modelView.updateTrailersData(mMovieEntry.getId());
            }

        }
    }

    private Intent createShareMovieIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mMovieEntry.toString())
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
        Picasso.get().load(mMovieEntry.getPosterUrl()).into(imageView);

        TextView textView = findViewById(R.id.tv_movie_title_details);
        textView.setText(mMovieEntry.getTitle());

        textView = findViewById(R.id.tv_movie_year_details);
        String releaseDate = mMovieEntry.getReleaseDate();
        if (!TextUtils.isEmpty(releaseDate)) {
            textView.setText(releaseDate.split("-")[0]); // show only the year for now
        } else {
            textView.setVisibility(View.GONE);
        }

        textView = findViewById(R.id.tv_movie_rating_details);
        float rating = mMovieEntry.getUserRating();
        textView.setText(getString(R.string.movie_rating_string, rating));

        textView = findViewById(R.id.tv_movie_overview_details);
        textView.setText(mMovieEntry.getOverview());
    }
}
