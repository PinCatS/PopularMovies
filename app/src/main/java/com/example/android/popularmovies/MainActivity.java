package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies_grid);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieClass().execute("movie/top_rated");
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
            if (movies != null) {
                mMovieAdapter.setMoviesData(movies);
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }
}
