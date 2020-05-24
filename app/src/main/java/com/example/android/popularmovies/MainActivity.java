package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.utilities.FakeMovieData;
import com.example.android.popularmovies.utilities.MovieJasonUtils;

import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            List<Movie> movies = MovieJasonUtils.createFromJsonString(FakeMovieData.getFakeMovieJsonString());
            mRecyclerView = findViewById(R.id.rv_movies_grid);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            MovieAdapter adapter = new MovieAdapter(this);
            adapter.setMoviesData(movies);
            mRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMovieClickListener(Movie movie) {
        Intent openMovieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        openMovieDetailsIntent.putExtra(Movie.EXTRA_MOVIE_PARCELABLE, movie);
        startActivity(openMovieDetailsIntent);
    }
}
