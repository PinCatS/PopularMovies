package com.example.android.popularmovies.ui.details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieTrailerEntry;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private final OnTrailerClickListener mTrailerClickListener;
    private List<MovieTrailerEntry> mTrailersData;

    TrailerAdapter(OnTrailerClickListener listener) {
        this.mTrailerClickListener = listener;
    }

    List<MovieTrailerEntry> getTrailerData() {
        return mTrailersData;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        // TODO: Do we really need that check ?
        if (mTrailersData != null && mTrailersData.size() > 0) {
            holder.trailerName.setText(mTrailersData.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mTrailersData == null ? 0 : mTrailersData.size();
    }

    public void setTrailersData(List<MovieTrailerEntry> trailers) {
        mTrailersData = trailers;
    }

    public interface OnTrailerClickListener {
        void onTrailerClickListener(MovieTrailerEntry movieTrailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerName;

        TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "MovieTrailer has been clicked");
            MovieTrailerEntry movieTrailer = mTrailersData.get(getAdapterPosition());
            mTrailerClickListener.onTrailerClickListener(movieTrailer);
        }
    }
}
