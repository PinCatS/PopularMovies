package com.example.android.popularmovies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            List<Movie> movies = DataUtils.createFromJsonString(FakeMovieData.getFakeMovieJsonString());
            mRecyclerView = findViewById(R.id.rv_movies_grid);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            MovieAdapter adapter = new MovieAdapter();
            adapter.setMoviesData(movies);
            mRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
