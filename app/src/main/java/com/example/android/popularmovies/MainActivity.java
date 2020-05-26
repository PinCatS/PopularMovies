package com.example.android.popularmovies;

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

import com.example.android.popularmovies.utilities.MovieJasonUtils;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {
    private static final String MOVIES_DATA_KEY = "movies";

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    ProgressBar mLoadingIndicator;
    TextView mErrorMessage;
    String mLastSelectedEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        mRecyclerView = findViewById(R.id.rv_movies_grid);
        final int columns = getResources().getInteger(R.integer.gallery_columns);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_DATA_KEY)) {
            loadMovieData(mLastSelectedEndpoint);
        } else {
            mMovieAdapter.setMoviesData(savedInstanceState.<Movie>getParcelableArrayList(MOVIES_DATA_KEY));
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(MOVIES_DATA_KEY, mMovieAdapter.getMoviesData());
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
    public void onMovieClickListener(Movie movie) {
        Intent openMovieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        openMovieDetailsIntent.putExtra(Movie.EXTRA_MOVIE_PARCELABLE, movie);
        startActivity(openMovieDetailsIntent);
    }

    public class FetchMovieClass extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params != null && params.length == 0) {
                return null;
            }

            String requestEndpoint = params[0];
            URL movieUrl = NetworkUtilities.buildURL(requestEndpoint);

            ArrayList<Movie> movies = null;
            try {
                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                movies = MovieJasonUtils.createFromJsonString(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mMovieAdapter.setMoviesData(movies);
                mMovieAdapter.notifyDataSetChanged();
                showMovieData();
            } else {
                showErrorMessage();
            }
        }
    }
}
