package com.example.android.popularmovies;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            List<Movie> movies = DataUtils.createFromJsonString(FakeMovieData.getFakeMovieJsonString());
            for (int i = 0; i < movies.size(); i++) {
                Log.i("MainActivity", "Movie: " + movies.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
