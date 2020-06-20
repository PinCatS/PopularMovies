package com.example.android.popularmovies.ui.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieReview;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<MovieReview> mReviewsData;

    ReviewAdapter() {
    }

    List<MovieReview> getReviewData() {
        return mReviewsData;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // TODO: Do we really need that check ?
        if (mReviewsData != null && mReviewsData.size() > 0) {
            holder.authorName.setText(mReviewsData.get(position).getName());
            holder.content.setText(mReviewsData.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mReviewsData == null ? 0 : mReviewsData.size();
    }

    public void setReviewsData(List<MovieReview> reviews) {
        mReviewsData = reviews;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorName;
        TextView content;

        ReviewViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.tv_review_author);
            content = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
