package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesImageAdapter;
import br.com.adalbertofjr.popularmovies.util.Constants;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * Popular Movies
 * MoviesFragment
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesFragment extends Fragment {

    private static final String MOVIES_INSTANCE_STATE = "movies";
    private MoviesImageAdapter mMoviesAdapter;
    private ArrayList<Movies> mMovies;
    private ProgressBar mMoviesProgressBar;
    private GridView mGridMovies;
    private TextView mErrorMessage;
    private ActionBar mToolbar;
    private String mFetchOption;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        mToolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (mToolbar != null) {
            mToolbar.setTitle(null);
        }

        mGridMovies = (GridView) rootView.findViewById(R.id.gv_movies_fragment);
        mMoviesProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_movies_progress);
        mErrorMessage = (TextView) rootView.findViewById(R.id.tv_movies_error_message);

        mMoviesAdapter = new MoviesImageAdapter(getActivity(), new ArrayList<Movies>());
        mGridMovies.setAdapter(mMoviesAdapter);

        return rootView;
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
        if (mMovies == null) {
            startFetchMoviesTask();
        } else {
            updateMoviesAdapter(mMovies);
        }
    }

    private void startFetchMoviesTask() {
        if (Util.isConnected(getActivity())) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute();
        } else {
            hideProgressBar();
            mGridMovies.setEmptyView(mErrorMessage);
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
                    mFetchOption = Constants.MOVIES_POPULAR_URL;
                } else {
                    mFetchOption = Constants.MOVIES_TOP_RATED_URL;
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
            startFetchMoviesTask();
        } else {
            hideProgressBar();
            mMoviesAdapter.clear();
            mGridMovies.setEmptyView(mErrorMessage);
        }
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movies>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<Movies> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonString;

            try {

                if (mFetchOption == null)
                    mFetchOption = Util.getOptionSortFetchMovies(getActivity());

                URL url = new URL(mFetchOption);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                moviesJsonString = buffer.toString();

                try {
                    return getMoviesDataFromJson(moviesJsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            updateMoviesAdapter(movies);
        }
    }

    private void updateMoviesAdapter(ArrayList<Movies> movies) {
        if (movies != null) {
            hideProgressBar();

            if (mMovies == null) mMovies = movies;

            mMoviesAdapter.clear();
            mMoviesAdapter.addAll(movies);
        }
    }

    private void hideProgressBar() {
        if (mMoviesProgressBar.getVisibility() == View.VISIBLE) {
            mMoviesProgressBar.setVisibility(View.GONE);
        }
    }

    private ArrayList<Movies> getMoviesDataFromJson(String moviesJsonString)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MOVIES_LIST_KEY);

        ArrayList<Movies> movies = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            String backdrop_path;
            String poster_path;
            String vote_average;
            String original_title;
            String release_date;
            String overview;

            JSONObject movieData = moviesArray.getJSONObject(i);

            backdrop_path = movieData.getString(Constants.MOVIES_BACKGROUND_KEY);
            poster_path = movieData.getString(Constants.MOVIES_POSTER_KEY);
            vote_average = movieData.getString(Constants.MOVIES_VOTE_AVERAGE_KEY);
            original_title = movieData.getString(Constants.MOVIES_TITLE_KEY);
            release_date = movieData.getString(Constants.MOVIES_RELEASE_DATE_KEY);
            overview = movieData.getString(Constants.MOVIES_OVERVIEW_KEY);

            Movies movie = new Movies(backdrop_path,
                    poster_path,
                    vote_average,
                    original_title,
                    release_date,
                    overview);

            movies.add(movie);
        }

        return movies;
    }

}
