package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.ui.DetailActivity;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * Popular Movies
 * MoviesImageAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesImageAdapter extends RecyclerView.Adapter<MoviesImageAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<Movies> mMovies;

    public MoviesImageAdapter(Context mContext, ArrayList<Movies> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
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
                startDetailMovie(movie);
            }
        });
    }

    private void startDetailMovie(Movies movie) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(Constants.MOVIE_DETAIL_EXTRA, movie);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movies_item_poster);
        }
    }
}
