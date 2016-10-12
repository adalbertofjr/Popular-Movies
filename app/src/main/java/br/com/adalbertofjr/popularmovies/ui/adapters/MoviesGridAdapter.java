package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviesGridAdapter.class.getSimpleName();
    private final Context mContext;
    private final ArrayList<Movies> mMovies;
    private final OnMovieSelectedListener mListener;

    public MoviesGridAdapter(Context mContext, ArrayList<Movies> mMovies, OnMovieSelectedListener mListener) {
        this.mContext = mContext;
        this.mMovies = mMovies;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movies movie = mMovies.get(position);

        Uri.Builder uriImage = Uri.parse(Constants.MOVIE_IMAGE_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(movie.getPoster_path());

        String urlPoster = uriImage.build().toString();

        Picasso.with(mContext).load(urlPoster).into(holder.posterImageView);

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMovieSelected(movie);
                } else {
                    Log.d(LOG_TAG, "You need MoviesFragment.OnMovieSelectedListener callback.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public interface OnMovieSelectedListener {
        void onMovieSelected(Movies movie);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movies_item_poster);
        }
    }
}
