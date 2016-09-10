package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Reviews;

/**
 * Popular Movies
 * ReviewsAdapter
 * <p>
 * Created by Adalberto Fernandes Júnior on 09/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Reviews> mReviews;

    public ReviewsAdapter(Context mContext, List<Reviews> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reviews reviews = mReviews.get(position);

        if (position > 0) {
            holder.mDivider.setVisibility(View.VISIBLE);
        }

        holder.mAuthorView.setText(reviews.getAuthor());
        holder.mContentView.setText(reviews.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mAuthorView;
        private final TextView mContentView;
        private final View mDivider;

        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorView = (TextView) itemView.findViewById(R.id.tv_review_item_author);
            mContentView = (TextView) itemView.findViewById(R.id.tv_review_item_content);
            mDivider = itemView.findViewById(R.id.vw_review_item_divider);
        }
    }
}
