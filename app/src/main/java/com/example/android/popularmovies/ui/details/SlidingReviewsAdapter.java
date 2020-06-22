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

/*
 * View pager adapter for sliding reviews in the MovieDetailsActivity
 * */
public class SlidingReviewsAdapter extends RecyclerView.Adapter<SlidingReviewsAdapter.ReviewViewHolder> {

    private List<MovieReview> mReviewList;

    SlidingReviewsAdapter(List<MovieReview> reviews) {
        mReviewList = reviews;
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
        if (mReviewList != null && mReviewList.size() > 0) {
            holder.authorName.setText(mReviewList.get(position).getName());
            holder.content.setText(mReviewList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
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
