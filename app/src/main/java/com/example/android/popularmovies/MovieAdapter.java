package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMoviesData;
    private final OnMovieClickListener mMovieClickListener;

    MovieAdapter(OnMovieClickListener listener) {
        this.mMovieClickListener = listener;
    }

    public interface OnMovieClickListener {
        void onMovieClickListener(Movie movie);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (mMoviesData != null) {
            Picasso.get().load(mMoviesData.get(position).getPosterImage()).into(holder.posterView);
        }
    }

    @Override
    public int getItemCount() {
        return mMoviesData == null ? 0 : mMoviesData.size();
    }

    ArrayList<Movie> getMoviesData() {
        return mMoviesData;
    }

    void setMoviesData(ArrayList<Movie> movies) {
        mMoviesData = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterView;

        MovieViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Movie movie = mMoviesData.get(getAdapterPosition());
            mMovieClickListener.onMovieClickListener(movie);
        }
    }
}
