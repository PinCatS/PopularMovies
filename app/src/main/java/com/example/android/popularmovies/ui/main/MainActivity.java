package com.example.android.popularmovies.ui.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieEntry;
import com.example.android.popularmovies.data.network.NetworkUtilities;
import com.example.android.popularmovies.ui.details.MovieDetailsActivity;
import com.example.android.popularmovies.utilities.MovieJasonUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private static final String MOVIES_DATA_KEY = "movies";
    private static final String LAST_SELECTED_ENDPOINT_KEY = "last_endpoint";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private String mLastSelectedEndpoint;

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

        /* If we have a saved movies after screen config change or user switched between apps
         *  we don't need to request data from the internet. So we are restoring saved movies
         *  and notify an adapter about it. Otherwise we load data from the internet */
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_DATA_KEY)) {
            loadMovieData(mLastSelectedEndpoint);
        } else {
            ArrayList<MovieEntry> movieEntries = savedInstanceState.getParcelableArrayList(MOVIES_DATA_KEY);
            mMovieAdapter.setMoviesData(movieEntries);
            mMovieAdapter.notifyDataSetChanged();
            if (savedInstanceState.containsKey(LAST_SELECTED_ENDPOINT_KEY)) {
                mLastSelectedEndpoint = savedInstanceState.getString(LAST_SELECTED_ENDPOINT_KEY);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(MOVIES_DATA_KEY, mMovieAdapter.getMoviesData());
        outState.putString(LAST_SELECTED_ENDPOINT_KEY, mLastSelectedEndpoint);
        super.onSaveInstanceState(outState);
    }

    private void loadMovieData(String endpoint) {
        if (TextUtils.isEmpty(endpoint)) {
            endpoint = NetworkUtilities.POPULAR_ENDPOINT;
        }
        new FetchMovieClass().execute(endpoint);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showMovieData() {
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
                loadMovieData(mLastSelectedEndpoint);
                return true;
            case R.id.most_popular_item:
                loadMovieData(NetworkUtilities.POPULAR_ENDPOINT);
                mLastSelectedEndpoint = NetworkUtilities.POPULAR_ENDPOINT;
                return true;
            case R.id.top_rated_item:
                loadMovieData(NetworkUtilities.TOP_RATED_ENDPOINT);
                mLastSelectedEndpoint = NetworkUtilities.TOP_RATED_ENDPOINT;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMovieClickListener(MovieEntry movieEntry) {
        Intent openMovieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        openMovieDetailsIntent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_PARCELABLE, movieEntry);
        startActivity(openMovieDetailsIntent);
    }

    class FetchMovieClass extends AsyncTask<String, Void, ArrayList<MovieEntry>> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieEntry> doInBackground(String... params) {
            if (params != null && params.length == 0) {
                return null;
            }

            String requestEndpoint = params[0];
            URL movieUrl = NetworkUtilities.buildURL(requestEndpoint);

            ArrayList<MovieEntry> movieEntries = null;
            try {
                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                movieEntries = MovieJasonUtils.createFromJsonString(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return movieEntries;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieEntry> movieEntries) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieEntries != null) {
                mMovieAdapter.setMoviesData(movieEntries);
                mMovieAdapter.notifyDataSetChanged();
                showMovieData();
            } else {
                showErrorMessage();
            }
        }
    }
}