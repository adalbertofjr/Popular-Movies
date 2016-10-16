package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * Popular Movies
 * MoviesGridAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesGridAdapter extends CursorRecyclerViewAdapter<MoviesGridAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviesGridAdapter.class.getSimpleName();
    private final Context mContext;
    private final OnMovieSelectedListener mListener;

    public MoviesGridAdapter(Context mContext, Cursor cursor, OnMovieSelectedListener mListener) {
        super(mContext, cursor);
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final Movies movie = new Movies();

        if (cursor.moveToNext()) {
            movie.setId(cursor.getString(0));
            movie.setOriginal_title(cursor.getString(1));
            movie.setPoster_path(cursor.getString(2));
            movie.setRelease_date(cursor.getString(3));
            movie.setVote_average(cursor.getString(4));
            movie.setOverview(cursor.getString(5));
            movie.setBackdrop_path(cursor.getString(6));
        } else {
            return;
        }

        Uri.Builder uriImage = Uri.parse(Constants.MOVIE_IMAGE_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(movie.getPoster_path());

        String urlPoster = uriImage.build().toString();

        Picasso.with(mContext).load(urlPoster).into(holder.posterImageView);

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMovieSelected(movie, null);
                } else {
                    Log.d(LOG_TAG, "You need MoviesFragment.OnMovieSelectedListener callback.");
                }
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_item, parent, false);

        return new ViewHolder(view);
    }

    public interface OnMovieSelectedListener {
        void onMovieSelected(Movies movie, Uri uri);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movies_item_poster);
        }
    }
}
