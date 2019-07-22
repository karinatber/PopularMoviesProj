package com.example.autotests.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autotests.popularmoviesapp.R;
import com.example.autotests.popularmoviesapp.model.reviews.Review;

import java.util.List;

/**
 * Created by karina.bernice on 12/01/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    final String TAG = getClass().getSimpleName();
    List<Review> mReviewData;

    public ReviewsAdapter(){

    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        Review review = mReviewData.get(position);

        holder.mReviewAuthor.setText(review.getAuthor());
        holder.mReviewBody.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviewData != null){
            return mReviewData.size();
        }
        return 0;
    }

    class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final Context mContext;
        final TextView mReviewBody;
        final TextView mReviewAuthor;
        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mReviewBody = (TextView) itemView.findViewById(R.id.tv_review_body);
            mReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }

    public void setReviewData(List<Review> reviewData){
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
