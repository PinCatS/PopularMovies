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
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData(mLastSelectedEndpoint);
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
        MenuInflater inflater = new MenuInflater(this);
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

    public class FetchMovieClass extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params != null && params.length == 0) {
                return null;
            }

            String requestEndpoint = params[0];
            URL movieUrl = NetworkUtilities.buildURL(requestEndpoint);

            List<Movie> movies = null;
            try {
                String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                movies = MovieJasonUtils.createFromJsonString(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
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
