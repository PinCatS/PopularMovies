package com.example.android.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.ui.details.MovieDetailsActivity;
import com.example.android.popularmovies.utilities.InjectorUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String MOVIES_DATA_KEY = "movies";
    private static final String LAST_SELECTED_VIEW_TYPE_KEY = "last_endpoint";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    private MainActivityViewModel mModelView;
    private Observer<List<MovieEntry>> mMoviesObserver;

    private boolean mOfflineMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        mRecyclerView = findViewById(R.id.rv_movies_grid);

        /* Setting recycler view with Grid Layout Manager.
         *  Number of columns depends on the screen configuration. It is a simple
         *  but not the best solution since it is better to vary number of columns according to the
         *  screen size.
         * */
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(layoutManager);

        /* The grid items content is not going to change in size, so we could set fixed for
         *  better performance */
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mMoviesObserver = newMovieEntries -> {
            /* Need to implement DiffUtils later to improve performance
             * in case have time before deadline
             * */
            mMovieAdapter.setMoviesData(newMovieEntries);
            mMovieAdapter.notifyDataSetChanged();

            if (newMovieEntries != null) showMovieData();
            else showLoading();
        };

        /*
         * We set up Model View here
         * We are passing view type (popular, top rated, favorite) to listen for the right live data
         * */
        MainActivityModelFactory modelViewFactory = InjectorUtils.provideMainActivityModelFactory(this);
        mModelView = new ViewModelProvider(this, modelViewFactory).get(MainActivityViewModel.class);
        mModelView.getMoviesLiveData(mModelView.getViewType()).observe(this, mMoviesObserver);

        // Subscribe to listen movies fetch failures
        mModelView.getMoviesFetchFailureLiveData().observe(this, isFailed -> {
            if (isFailed) {
                Toast.makeText(MainActivity.this, "Failed to retrieve movies", Toast.LENGTH_LONG).show();
                switchToOfflineMode();
            }
        });
    }

    /*
     * Switch to offline mode (is called when there are fetch failures)
     * */
    private void switchToOfflineMode() {
        // Request view only if we not in it already
        if (!mModelView.getViewType().equals(MainActivityViewModel.FAVORITES_VIEW)) {
            Toast.makeText(MainActivity.this, "Switching to favorites", Toast.LENGTH_LONG).show();
            switchToView(MainActivityViewModel.FAVORITES_VIEW);
            mModelView.updateMovieData();
        }
        mOfflineMode = true;
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showMovieData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.refresh_item:
                // Requests to retrieve data from a data source if it is from network (not db)
                mModelView.updateMovieData();
                return true;
            case R.id.most_popular_item:
                if (!mModelView.getViewType().equals(MainActivityViewModel.POPULAR_VIEW)) {
                    switchToView(MainActivityViewModel.POPULAR_VIEW);
                    mModelView.updateMovieData();
                }
                return true;
            case R.id.top_rated_item:
                if (!mModelView.getViewType().equals(MainActivityViewModel.TOP_RATED_VIEW)) {
                    switchToView(MainActivityViewModel.TOP_RATED_VIEW);
                    mModelView.updateMovieData();
                }
                return true;
            case R.id.favorites_item:
                if (!mModelView.getViewType().equals(MainActivityViewModel.FAVORITES_VIEW)) {
                    switchToView(MainActivityViewModel.FAVORITES_VIEW);
                    //mModelView.updateMovieData();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Switch to one of the view to observe data for appropriate view
     *  View types are:
     *       MainActivityViewModel.POPULAR_VIEW
     *       MainActivityViewModel.TOP_RATED_VIEW
     *       MainActivityViewModel.FAVORITE_VIEW
     * */
    private void switchToView(String endpointView) {
        mModelView.getMoviesLiveData(mModelView.getViewType()).removeObserver(mMoviesObserver);
        mModelView.setViewType(endpointView);
        /* If we were in offline mode, means that we had issue with fetch
         * try to fetch data again before switching to the observer,
         * otherwise we always loop in an offline mode
         */
        if (mOfflineMode) {
            mOfflineMode = false;
            mModelView.updateMovieData();
        }
        mModelView.getMoviesLiveData(endpointView).observe(this, mMoviesObserver);
    }

    @Override
    public void onMovieClickListener(MovieEntry movieEntry) {
        Intent openMovieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        openMovieDetailsIntent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_PARCELABLE, movieEntry);
        startActivity(openMovieDetailsIntent);
    }
}
