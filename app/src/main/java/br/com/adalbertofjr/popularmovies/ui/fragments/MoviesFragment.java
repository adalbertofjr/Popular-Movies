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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.tasks.FetchMoviesTask;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesGridAdapter;
import br.com.adalbertofjr.popularmovies.util.Constants;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * Popular Movies
 * MoviesFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesFragment extends Fragment
        implements MoviesGridAdapter.OnMovieSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String MOVIES_INSTANCE_STATE = "movies";
    public static final String MOVIE_FRAGMENT_TAG = "MFTAG";
    private static final int MOVIES_LOADER = 0;
    private MoviesGridAdapter mMoviesAdapter;
    private ArrayList<Movies> mMovies;
    private ProgressBar mMoviesProgressBar;
    private TextView mErrorMessage;
    private ActionBar mToolbar;
    private String mFetchOption;
    private RecyclerView mGridMoviesRecyclerView;
    private boolean mTwoPane;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_INSTANCE_STATE)) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_INSTANCE_STATE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        initToolbar();

        mGridMoviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movies_fragment);
        mMoviesProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_movies_progress);
        mErrorMessage = (TextView) rootView.findViewById(R.id.tv_movies_error_message);

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

    private void initToolbar() {
        mToolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mToolbar.setDisplayShowTitleEnabled(false);
        mToolbar.setLogo(R.mipmap.ic_pm_logo);
        mToolbar.setDisplayUseLogoEnabled(true);
        mToolbar.setDisplayShowHomeEnabled(true);
    }

    private String getTitleToolbar() {
        String sortOptionPreference = Util.getSortOptionPreference(getActivity());
        if (sortOptionPreference.equals(Constants.MOVIES_POPULAR_PATH)) {
            return getResources().getStringArray(R.array.pref_sort_options)[0];
        }

        return getResources().getStringArray(R.array.pref_sort_options)[1];
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesAdapter();
    }

    private void startFetchMoviesTask() {
        if (Util.isConnected(getActivity())) {
            FetchMoviesTask moviesTask = new FetchMoviesTask(getActivity());
            moviesTask.execute(mFetchOption);
            getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        } else {
            hideProgressBar();
            // Todo - Corrigir mensagens de erro de conexão.
            //mGridMoviesRecyclerView.setEmptyView(mErrorMessage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_INSTANCE_STATE, mMovies);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movies_fragment, menu);

        MenuItem item = menu.findItem(R.id.action_fetch);
        Spinner spnFetchMovies = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mToolbar.getThemedContext(),
                R.array.pref_sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnFetchMovies.setAdapter(adapter);

        spnFetchMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long position) {
                String option = getResources().getStringArray(R.array.pref_sort_options_values)[(int) position];

                if (option.equals(Constants.MOVIES_POPULAR_PATH)) {
                    mFetchOption = option;
                } else {
                    mFetchOption = Constants.MOVIES_TOP_RATED_PATH;
                }

                startFetchMoviesTask();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            refreshFetchMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshFetchMovies() {
        if (Util.isConnected(getActivity())) {
            mMoviesAdapter.notifyDataSetChanged();
        } else {
            hideProgressBar();
            // Todo - Corrigir mensagens de erro de conexão.
            //mMoviesAdapter.clear();
            //mGridMovies.setEmptyView(mErrorMessage);
        }
    }

    @Override
    public void onMovieSelected(Movies movie, Uri uri) {
        Uri contentUri;

        if (mFetchOption.equals(Constants.MOVIES_POPULAR_PATH)) {
            contentUri = MoviesContract.PopularEntry.CONTENT_URI;
        } else {
            contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
        }

        ((MoviesGridAdapter.OnMovieSelectedListener) getActivity())
                .onMovieSelected(movie, contentUri);
    }

    private void updateMoviesAdapter() {
        hideProgressBar();

        mMoviesAdapter = new MoviesGridAdapter(getActivity(), null, this);
        mGridMoviesRecyclerView.setAdapter(mMoviesAdapter);

        if (mTwoPane) {
            Uri contentUri;

            if (mFetchOption.equals(Constants.MOVIES_POPULAR_PATH)) {
                contentUri = MoviesContract.PopularEntry.CONTENT_URI;
            } else {
                contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
            }

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
        Uri contentUri;

        if (mFetchOption.equals(Constants.MOVIES_POPULAR_PATH)) {
            contentUri = MoviesContract.PopularEntry.CONTENT_URI;
        } else {
            contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
        }

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
