package com.example.android.popularmovies.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieReview;
import com.example.android.popularmovies.data.database.MovieTrailerEntry;
import com.example.android.popularmovies.data.database.MovieTrailerHolder;
import com.example.android.popularmovies.utilities.InjectorUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_PARCELABLE = "Movie";

    private MovieEntry mMovieEntry;

    private FloatingActionButton mFavoriteFab;
    private boolean mIsFavorite;

    private RecyclerView mTrailersRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private MovieDetailsViewModel mModelView;
    private Toast mFavoriteToast;

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

        mFavoriteFab = findViewById(R.id.bt_favorite);

        // Setup recycler view for movie trailers
        mTrailersRecyclerView = findViewById(R.id.rv_movie_trailers_list);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecor = new DividerItemDecoration(mTrailersRecyclerView.getContext(), trailersLayoutManager.getOrientation());
        mTrailersRecyclerView.addItemDecoration(itemDecor);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

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
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                } else {
                    Log.d(TAG, "Not Favorite");
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_make_favorite));
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
                    initSlider(newReviews);
                }
                /*mReviewAdapter.setReviewsData(newReviews);
                mReviewAdapter.notifyDataSetChanged();*/
            });

            // Retrieve movie trailers and reviews
            mModelView.updateTrailersData(mMovieEntry.getId());
            mModelView.updateReviewsData(mMovieEntry.getId());

            populateUI();
        }

        mFavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFavorite) {
                    mModelView.removeFromFavorite(mMovieEntry);
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_make_favorite));
                    showToastMessage(R.string.remove_from_favorite_string);
                } else {
                    mModelView.saveAsFavorite(mMovieEntry);
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                    showToastMessage(R.string.mark_as_favorite_string);
                }
            }
        });
    }

    private void showToastMessage(int p) {
        if (mFavoriteToast != null) {
            mFavoriteToast.cancel();
        }
        mFavoriteToast = Toast.makeText(MovieDetailsActivity.this, p, Toast.LENGTH_SHORT);
        mFavoriteToast.show();
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

    /*
     * Initializes image slider
     *
     * Basically it sets up view pager with an adapter and sets circle indicators
     * if there is only review, it removes indicator layout
     * */
    private void initSlider(List<MovieReview> reviews) {

        ViewPager2 mPager = findViewById(R.id.pager_reviews);


        SlidingReviewsAdapter.OnReadMoreClickListener readMoreClickListener = new SlidingReviewsAdapter.OnReadMoreClickListener() {
            @Override
            public void onReadMoreClick(MovieReview review) {
                Uri reviewUri = Uri.parse(review.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, reviewUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        };

        mPager.setAdapter(new SlidingReviewsAdapter(reviews, readMoreClickListener));

        CircleIndicator3 indicator = findViewById(R.id.indicator);

        if (reviews.size() == 1) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(mPager);
            final float density = getResources().getDisplayMetrics().density;
        }
    }
}
