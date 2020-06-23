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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.database.MovieReview;
import com.example.android.popularmovies.data.database.MovieTrailer;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.example.android.popularmovies.utilities.InjectorUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

/* The movie activity displays detailed information about the movie
 *  The activity is opened by MainActivity when clicked on the movie poster
 *  Movie is passed to the activity via parcel.
 *
 *  ModelView is created in onCreate method. It is the only time when there is a fetch from the network
 *       1. Movie trailers
 *       2. Movie reviews
 *
 * */
public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_PARCELABLE = "Movie";

    private MovieEntry mMovieEntry;
    private TrailerAdapter mTrailerAdapter;
    private MovieDetailsViewModel mModelView;
    private FloatingActionButton mFavoriteFab;
    private boolean mIsFavorite;
    private Toast mFavoriteToast;

    ActivityMovieDetailsBinding mBinding;

    @Override
    public void onTrailerClickListener(MovieTrailer movieTrailer) {
        openYotubeTrailer(movieTrailer.getKey());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        // Enable UP button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        mFavoriteFab = mBinding.includeMovieMainInfo.btFavorite;

        // Setup recycler view for movie trailers
        RecyclerView mTrailersRecyclerView = mBinding.detailsMovieTrailers.rvMovieTrailersList;

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

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

            // Creating a ModelView for MovieDetails activity
            MovieDetailsModelFactory modelViewFactory = InjectorUtils.provideMovieDetailsModelFactory(this, mMovieEntry.getId());
            mModelView = new ViewModelProvider(this, modelViewFactory).get(MovieDetailsViewModel.class);

            /* We listen for event when user marks as or remove from favorite.
             *  As soon as that happens, we update the UI
             *  When model view is created initially, it requests a check if the movie is marked as
             *  favorite */
            mModelView.isFavorite().observe(this, isFavorite -> {
                mIsFavorite = isFavorite;
                if (isFavorite) {
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                } else {
                    mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_make_favorite));
                }
            });

            /* Here we listen for new trailers
             *  Trailers are requested from a network only during creation of Model View
             *  Config changes doesn't do a call to network
             * */
            mModelView.getMovieTrailersLiveData().observe(this, newTrailers -> {
                mMovieEntry.setTrailers(newTrailers);
                mTrailerAdapter.setTrailersData(newTrailers);
                mTrailerAdapter.notifyDataSetChanged();
            });

            /* Here we listen for new reviews
             *  Reviews are requested from a network only during creation of Model View
             *  Config changes doesn't do a call to network
             * */
            mModelView.getMovieReviewsLiveData().observe(this, this::initSlider);

            populateUI();
        }

        mFavoriteFab.setOnClickListener(view -> {
            if (mIsFavorite) {
                mModelView.removeFromFavorite(mMovieEntry);
                mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_make_favorite));
                showToastMessage(R.string.remove_from_favorite_string);
            } else {
                mModelView.saveAsFavorite(mMovieEntry);
                mFavoriteFab.setImageDrawable(getDrawable(R.drawable.ic_favorite));
                showToastMessage(R.string.mark_as_favorite_string);
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
        ImageView imageView = mBinding.includeMovieMainInfo.ivPosterDetails;
        Picasso.get().load(mMovieEntry.getPosterUrl()).into(imageView);

        TextView textView = mBinding.tvMovieTitleDetails;
        textView.setText(mMovieEntry.getTitle());

        textView = mBinding.includeMovieMainInfo.tvMovieYearDetails;
        String releaseDate = mMovieEntry.getReleaseDate();
        if (!TextUtils.isEmpty(releaseDate)) {
            textView.setText(releaseDate.split("-")[0]); // show only the year for now
        } else {
            textView.setVisibility(View.GONE);
        }

        textView = mBinding.includeMovieMainInfo.tvMovieRatingDetails;
        float rating = mMovieEntry.getUserRating();
        textView.setText(getString(R.string.movie_rating_string, rating));

        textView = mBinding.includeMovieMainInfo.tvMovieOverviewDetails;
        textView.setText(mMovieEntry.getOverview());
    }

    /*
     * Opens a trailer in a browser/youtube by trailer key
     * */
    private void openYotubeTrailer(String trailerKey) {

        Uri trailerUri = Uri.parse(MovieTrailer.TRAILER_YOUTUBE_BASE_URL).buildUpon()
                .appendEncodedPath("watch")
                .appendQueryParameter("v", trailerKey)
                .build();

        Log.d(TAG, "Opening youtube trailer: " + trailerUri.toString());

        openLink(trailerUri);
    }

    /*
     * Initializes movie reviews card slider
     * */
    private void initSlider(List<MovieReview> reviews) {

        ViewPager2 mPager = mBinding.pagerReviews;
        CircleIndicator3 indicator = mBinding.indicator;

        // if there is no any reviews, we remove the views for reviews
        if (reviews == null || reviews.size() == 0) {
            mPager.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
            return;
        } else {
            mPager.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.VISIBLE);
        }


        // Open full review in a browser when ...read more is clicked
        SlidingReviewsAdapter.OnReadMoreClickListener readMoreClickListener = review -> {
            Uri reviewUri = Uri.parse(review.getUrl());
            openLink(reviewUri);
        };

        // setup adapter and indicators
        mPager.setAdapter(new SlidingReviewsAdapter(reviews, readMoreClickListener));

        if (reviews.size() == 1) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setViewPager(mPager);
        }
    }

    private void openLink(Uri linkUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, linkUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
