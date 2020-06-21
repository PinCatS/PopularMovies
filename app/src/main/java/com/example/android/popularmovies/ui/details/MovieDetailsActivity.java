package com.example.android.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieTrailerEntry;
import com.example.android.popularmovies.data.database.MovieTrailerHolder;
import com.example.android.popularmovies.utilities.InjectorUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_PARCELABLE = "Movie";

    private MovieEntry mMovieEntry;

    private Button mFavoriteButton;
    private boolean mIsFavorite;

    private RecyclerView mTrailersRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mReviewsRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private MovieDetailsViewModel mModelView;

    @Override
    public void onTrailerClickListener(MovieTrailerEntry movieTrailer) {
        Log.d(TAG, "TrailerClickListener invoked");
        openYotubeTrailer(movieTrailer.getKey());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Enable UP button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        mFavoriteButton = findViewById(R.id.bt_favorite);

        // Setup recycler view for movie trailers
        mTrailersRecyclerView = findViewById(R.id.rv_movie_trailers_list);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecor = new DividerItemDecoration(mTrailersRecyclerView.getContext(), trailersLayoutManager.getOrientation());
        mTrailersRecyclerView.addItemDecoration(itemDecor);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        // Setup recycler view for movie reviews
        mReviewsRecyclerView = findViewById(R.id.rv_movie_reviews_list);

        RecyclerView.LayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);

        mReviewAdapter = new ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        // Retrieve the movie info passed from the main activity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_MOVIE_PARCELABLE)) {
                mMovieEntry = intent.getParcelableExtra(EXTRA_MOVIE_PARCELABLE);
            }
        }

        if (mMovieEntry != null) {
            MovieDetailsModelFactory modelViewFactory = InjectorUtils.provideMovieDetailsModelFactory(this, mMovieEntry.getId());
            mModelView = new ViewModelProvider(this, modelViewFactory).get(MovieDetailsViewModel.class);

            mModelView.isFavorite().observe(this, isFavorite -> {
                mIsFavorite = isFavorite;
                if (isFavorite) {
                    Log.d(TAG, "Favorite");
                    mFavoriteButton.setText("Remove\nfrom favorite");
                    mFavoriteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                } else {
                    Log.d(TAG, "Not Favorite");
                    mFavoriteButton.setText("Mark\nas favorite");
                    mFavoriteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                }
            });

            mModelView.getMovieTrailersLiveData().observe(this, newTrailerHolders -> {
                View divider = findViewById(R.id.divider_trailers);
                TextView textView = findViewById(R.id.tv_trailers_label);
                if (newTrailerHolders.size() == 0) {
                    divider.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                } else {
                    divider.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }

                int movieId = mMovieEntry.getId();
                List<MovieTrailerEntry> trailers = new ArrayList<>();
                for (MovieTrailerHolder holder : newTrailerHolders) {
                    trailers.add(new MovieTrailerEntry(movieId, holder));
                }
                mMovieEntry.setTrailers(trailers);
                mTrailerAdapter.setTrailersData(trailers);
                mTrailerAdapter.notifyDataSetChanged();
            });

            mModelView.getMovieReviewsLiveData().observe(this, newReviews -> {
                View divider = findViewById(R.id.divider_reviews);
                if (newReviews.size() == 0) {
                    divider.setVisibility(View.GONE);
                } else {
                    divider.setVisibility(View.VISIBLE);
                }
                mReviewAdapter.setReviewsData(newReviews);
                mReviewAdapter.notifyDataSetChanged();
            });

            // Retrieve movie trailers and reviews
            mModelView.updateTrailersData(mMovieEntry.getId());
            mModelView.updateReviewsData(mMovieEntry.getId());

            populateUI();
        }

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFavorite) {
                    mModelView.removeFromFavorite(mMovieEntry);
                    mFavoriteButton.setText("Mark\nas favorite");
                    mFavoriteButton.setBackgroundColor(ContextCompat.getColor(MovieDetailsActivity.this, R.color.colorAccent));
                } else {
                    mModelView.saveAsFavorite(mMovieEntry);
                    mFavoriteButton.setText("Remove\nfrom favorite");
                    mFavoriteButton.setBackgroundColor(ContextCompat.getColor(MovieDetailsActivity.this, R.color.colorPrimary));
                }
            }
        });
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

    private void openYotubeTrailer(String trailerKey) {

        Uri trailerUri = Uri.parse(MovieTrailerEntry.TRAILER_YOUTUBE_BASE_URL).buildUpon()
                .appendEncodedPath("watch")
                .appendQueryParameter("v", trailerKey)
                .build();


        Log.d(TAG, "Opening youtube trailer: " + trailerUri.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
