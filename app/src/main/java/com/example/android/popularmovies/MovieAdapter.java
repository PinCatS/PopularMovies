package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMoviesData;

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

    void setMoviesData(List<Movie> movies) {
        mMoviesData = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterView;

        MovieViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.iv_poster);
        }
    }
}
