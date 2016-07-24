package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.util.Constants;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * Popular Movies
 * DetailFragment
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment {
    private Movies mMovie;
    private ProgressBar mProgressBar;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(Movies movie) {
        DetailMovieFragment dmf = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MOVIE_DETAIL_EXTRA, movie);
        dmf.setArguments(bundle);

        return dmf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = getArguments().getParcelable(Constants.MOVIE_DETAIL_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        TextView errorMessage = (TextView) rootView.findViewById(R.id.tv_detail_error_message);

        if (mMovie != null) {
            if (Util.isConnected(getActivity())) {
                Picasso.with(getContext())
                        .load(mMovie.getBackDropUrlPath())
                        .into((ImageView) rootView.findViewById(R.id.iv_detail_background), new Callback() {
                            @Override
                            public void onSuccess() {
                                hideProgressBar();
                                Picasso.with(getContext()).load(mMovie.getPosterUrlPath()).into((ImageView) rootView.findViewById(R.id.iv_detail_poster));
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
            } else {
                hideProgressBar();
                errorMessage.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    private void hideProgressBar() {
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
