package com.example.android.popularmovies.ui.details;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.database.MovieReview;

import java.util.List;

/*
 * View pager adapter for sliding reviews in the MovieDetailsActivity
 * */
public class SlidingReviewsAdapter extends PagerAdapter {

    private List<MovieReview> mReviewList;
    private LayoutInflater mInflater;

    SlidingReviewsAdapter(Context context, List<MovieReview> reviews) {
        mReviewList = reviews;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mReviewList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View reviewView = mInflater.inflate(R.layout.movie_review_item, view, false);

        final TextView authorName = reviewView.findViewById(R.id.tv_review_author);
        final TextView content = reviewView.findViewById(R.id.tv_review_content);


        authorName.setText(mReviewList.get(position).getName());
        content.setText(mReviewList.get(position).getContent());

        view.addView(reviewView, 0);

        return reviewView;
    }

    @Override
    public boolean isViewFromObject(View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
