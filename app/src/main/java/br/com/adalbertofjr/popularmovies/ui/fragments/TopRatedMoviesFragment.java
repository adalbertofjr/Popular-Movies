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
 * TopRatedMoviesFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 13/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TopRatedMoviesFragment extends Fragment implements MoviesGridAdapter.OnMovieSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String MOVIE_SELECTED_STATE = "movie_selected";
    private static final String MOVIE_POSITION_STATE = "movie_position";
    private static final int MOVIES_LOADER = 0;
    private MoviesGridAdapter mMoviesAdapter;
    private ArrayList<Movie> mMovies;
    private ProgressBar mMoviesProgressBar;
    private TextView mErrorMessage;
    private ActionBar mToolbar;
    private String mFetchOption;
    private RecyclerView mGridMoviesRecyclerView;
    private boolean mTwoPane;
    private Movie mMovieSelected;
    private int mPosition = RecyclerView.NO_POSITION;

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

        getExtrasSavedInstance(savedInstanceState);


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
    public void onStart() {
        super.onStart();

        checkConnection();
    }

    private void checkConnection() {
        if (!Util.isConnected(getActivity())) {
            mErrorMessage.setVisibility(View.VISIBLE);
            mGridMoviesRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessage.setVisibility(View.INVISIBLE);
            mGridMoviesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void getExtrasSavedInstance(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getParcelable(MOVIE_SELECTED_STATE) != null) {
            mMovieSelected = savedInstanceState.getParcelable(MOVIE_SELECTED_STATE);

            if (mMovieSelected != null) {
                mMoviesAdapter.setMovieSelected(mMovieSelected);
            }
        }

        if (savedInstanceState != null && savedInstanceState.getInt(MOVIE_POSITION_STATE) != RecyclerView.NO_POSITION) {
            mPosition = savedInstanceState.getInt(MOVIE_POSITION_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MOVIE_SELECTED_STATE, mMovieSelected);
        outState.putInt(MOVIE_POSITION_STATE, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieSelected(Movie movie, Uri uri) {
        Uri contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
        mMovieSelected = movie;

        ((MoviesGridAdapter.OnMovieSelectedListener) getActivity())
                .onMovieSelected(movie, contentUri);
    }

    @Override
    public void onMoviePosition(int position) {
        mPosition = position;
    }

    private void updateMoviesAdapter() {
        hideProgressBar();

        mMoviesAdapter = new MoviesGridAdapter(getActivity(), null, this);
        mGridMoviesRecyclerView.setAdapter(mMoviesAdapter);

        if (mTwoPane) {
            Uri contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
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
        Uri contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;

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

        if (mPosition != RecyclerView.NO_POSITION) {
            mGridMoviesRecyclerView.scrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }
}
