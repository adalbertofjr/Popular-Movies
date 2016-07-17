package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;

/**
 * Popular Movies
 * DetailFragment
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment {
    private Movies mMovie;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(Movies movie) {
        DetailMovieFragment dmf = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        dmf.setArguments(bundle);

        return dmf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = (Movies) getArguments().getParcelable("movie");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        String MOVIE_BACKDROP_URL = "http://image.tmdb.org/t/p/w500/";
        Uri.Builder uriBackdrop = Uri.parse(MOVIE_BACKDROP_URL)
                .buildUpon()
                .appendEncodedPath(mMovie.getBackdrop_path());

        String urlBackDrop = uriBackdrop.build().toString();

        String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
        Uri.Builder uriPoster = Uri.parse(MOVIE_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(mMovie.getPoster_path());

        final String urlPoster = uriPoster.build().toString();

        Picasso.with(getContext())
                .load(urlBackDrop)
                .into((ImageView) rootView.findViewById(R.id.iv_detail_backgrouns), new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(getContext()).load(urlPoster).into((ImageView) rootView.findViewById(R.id.iv_detail_poster));
                        ((TextView) rootView.findViewById(R.id.tv_detail_title)).setText(mMovie.getOriginal_title());
                        ((TextView) rootView.findViewById(R.id.tv_detail_title)).setText(mMovie.getOriginal_title());
                        ((TextView) rootView.findViewById(R.id.tv_detail_dt_release)).setText(mMovie.getRelease_date());
                        ((TextView) rootView.findViewById(R.id.tv_detail_vote_average)).setText(mMovie.getVote_average());
                        ((TextView) rootView.findViewById(R.id.tv_detail_overview)).setText(mMovie.getOverview());
                    }

                    @Override
                    public void onError() {

                    }
                });


        return rootView;
    }
}
