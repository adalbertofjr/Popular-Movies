package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesGridAdapter;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * PopularMovies
 * FavoritesMoviesFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 13/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class FavoritesMoviesFragment extends Fragment
        implements MoviesGridAdapter.OnMovieSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String MOVIES_INSTANCE_STATE = "movies";
    public static final String MOVIE_FRAGMENT_TAG = "MFTAG";
    private static final int MOVIES_LOADER = 0;
    private MoviesGridAdapter mMoviesAdapter;
    private ArrayList<Movie> mMovies;
    private ProgressBar mMoviesProgressBar;
    private TextView mErrorMessage;
    private ActionBar mToolbar;
    private String mFetchOption;
    private RecyclerView mGridMoviesRecyclerView;
    private boolean mTwoPane;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mGridMoviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies_fragment);
        mMoviesProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_movies_progress);
        mErrorMessage = (TextView) rootView.findViewById(R.id.tv_movies_error_message);

        mMoviesAdapter = new MoviesGridAdapter(getActivity(), null, this);
        mGridMoviesRecyclerView.setAdapter(mMoviesAdapter);

        RecyclerView.LayoutManager gridLayout;

        boolean isTablet = getActivity().getResources().getBoolean(R.bool.isTablet);
        mTwoPane = getActivity().getResources().getBoolean(R.bool.has_two_panes);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!isTablet) {
                gridLayout = new GridLayoutManager(getActivity(), 2);
            } else {
                gridLayout = new GridLayoutManager(getActivity(), 3);
            }
        } else {
            if (mTwoPane) {
                gridLayout = new GridLayoutManager(getActivity(), 2);
            } else {
                gridLayout = new GridLayoutManager(getActivity(), 4);
            }
        }

        mGridMoviesRecyclerView.setLayoutManager(gridLayout);
        mGridMoviesRecyclerView.setHasFixedSize(true);

        mFetchOption = Util.getSortOptionPreference(getActivity());

        return rootView;
    }

    @Override
    public void onMovieSelected(Movie movie, Uri uri) {
        Uri contentUri = MoviesContract.FavoritesEntry.CONTENT_URI;

        ((MoviesGridAdapter.OnMovieSelectedListener) getActivity())
                .onMovieSelected(movie, contentUri);
    }

    private void updateMoviesAdapter() {
        hideProgressBar();

        mMoviesAdapter = new MoviesGridAdapter(getActivity(), null, this);
        mGridMoviesRecyclerView.setAdapter(mMoviesAdapter);

        if (mTwoPane) {
            Uri contentUri = MoviesContract.PopularEntry.CONTENT_URI;
            onMovieSelected(mMovies.get(0), contentUri);
        }
    }

    private void hideProgressBar() {
        if (mMoviesProgressBar.getVisibility() == View.VISIBLE) {
            mMoviesProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = MoviesContract.FavoritesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                contentUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
