package com.example.android.popularmovies.ui.details;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
 * Adapter for sliding reviews in the MovieDetailsActivity
 * */
public class SlidingReviewsAdapter extends RecyclerView.Adapter<SlidingReviewsAdapter.ReviewViewHolder> {

    private static final String ELLIPSIS = "...Read more";
    private static final int REVIEW_TRIM_LENGTH = 300;
    private List<MovieReview> mReviewList;
    private OnReadMoreClickListener mReadMoreListener;

    SlidingReviewsAdapter(List<MovieReview> reviews, OnReadMoreClickListener listener) {
        mReviewList = reviews;
        mReadMoreListener = listener;
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

        holder.authorName.setText(holder.authorName.getContext()
                .getString(R.string.review_by_string, mReviewList.get(position).getName()));

        String originalText = mReviewList.get(position).getContent();

        /* Usually reviews are big, so we trim the original text and append ELLIPSIS span text
         *  that looks like a link and on click will do anything that is specified by the client
         *  activity. In our case it will open full review in a browser.
         * */
        SpannableString trimmedSpannableString = getTrimmedSpannableString(originalText, REVIEW_TRIM_LENGTH);
        if (trimmedSpannableString == null) {
            holder.content.setText(originalText);
        } else {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    mReadMoreListener.onReadMoreClick(mReviewList.get(position));
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };

            trimmedSpannableString.setSpan(clickableSpan, REVIEW_TRIM_LENGTH + 1, REVIEW_TRIM_LENGTH + ELLIPSIS.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.content.setText(trimmedSpannableString);
            holder.content.setMovementMethod(LinkMovementMethod.getInstance());
            holder.content.setHighlightColor(Color.TRANSPARENT);
        }

    }

    /* Trims the text leaving only the text of size trimLength and appends ELLIPSIS
     *
     * */
    private SpannableString getTrimmedSpannableString(String originalText, int trimLength) {
        if (originalText.length() > trimLength) {
            String trimmedText = originalText.substring(0, trimLength + 1);
            return new SpannableString(trimmedText + ELLIPSIS);
        } else {
            return null;
        }
    }

    interface OnReadMoreClickListener {
        void onReadMoreClick(MovieReview review);
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorName;
        TextView content;

        ReviewViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.tv_review_author);
            content = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
