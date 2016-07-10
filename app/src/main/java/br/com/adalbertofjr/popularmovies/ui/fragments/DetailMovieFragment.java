package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.adalbertofjr.popularmovies.R;

/**
 * Popular Movies
 * DetailFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment{
    private String mMovie;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(String s){
        DetailMovieFragment dmf = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TEXT, s);
        dmf.setArguments(bundle);

        return dmf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = getArguments().getString(Intent.EXTRA_TEXT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        ((TextView)rootView.findViewById(R.id.tv_movie)).setText(mMovie);

        return rootView;
    }
}
