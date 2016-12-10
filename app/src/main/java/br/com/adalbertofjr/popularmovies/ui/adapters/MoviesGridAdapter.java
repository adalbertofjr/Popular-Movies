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
import br.com.adalbertofjr.popularmovies.model.Movie;
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

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_TITLE = 1;
    public static final int COLUMN_POSTER = 2;
    public static final int COLUMN_RELEASE_DATE = 3;
    public static final int COLUMN_AVERAGE_VOTE = 4;
    public static final int COLUMN_OVERVIEW = 5;
    public static final int COLUMN_BACKDROP = 6;

    private final Context mContext;
    private final OnMovieSelectedListener mListener;
    private View mOldMovieSelectedView;
    private Movie mMovieSelected;

    public MoviesGridAdapter(Context mContext, Cursor cursor, OnMovieSelectedListener mListener) {
        super(mContext, cursor);
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        final Movie movie = new Movie();

        movie.setId(cursor.getString(COLUMN_ID));
        movie.setOriginal_title(cursor.getString(COLUMN_TITLE));
        movie.setPoster_path(cursor.getString(COLUMN_POSTER));
        movie.setRelease_date(cursor.getString(COLUMN_RELEASE_DATE));
        movie.setVote_average(cursor.getString(COLUMN_AVERAGE_VOTE));
        movie.setOverview(cursor.getString(COLUMN_OVERVIEW));
        movie.setBackdrop_path(cursor.getString(COLUMN_BACKDROP));

        Uri.Builder uriImage = Uri.parse(Constants.MOVIE_IMAGE_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(movie.getPoster_path());

        String urlPoster = uriImage.build().toString();

        Picasso.with(mContext).load(urlPoster).into(holder.posterImageView);

        if (mMovieSelected != null && mMovieSelected.getId().equals(movie.getId())) {
            mOldMovieSelectedView = holder.movieSelectedView;
            holder.movieSelectedView.setVisibility(View.VISIBLE);
        } else {
            holder.movieSelectedView.setVisibility(View.INVISIBLE);
        }

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMovieSelected(movie, null);
                    mListener.onMoviePosition(holder.getAdapterPosition());
                    mMovieSelected = movie;

                    if (mOldMovieSelectedView != null) {
                        mOldMovieSelectedView.setVisibility(View.INVISIBLE);
                    }

                    holder.movieSelectedView.setVisibility(View.VISIBLE);
                    mOldMovieSelectedView = holder.movieSelectedView;
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

    public void setMovieSelected(Movie movieSelected) {
        this.mMovieSelected = movieSelected;
    }

    public interface OnMovieSelectedListener {
        void onMovieSelected(Movie movie, Uri uri);

        void onMoviePosition(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;
        private final View movieSelectedView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movies_item_poster);
            movieSelectedView = itemView.findViewById(R.id.vw_movies_item_selected);
        }
    }
}
