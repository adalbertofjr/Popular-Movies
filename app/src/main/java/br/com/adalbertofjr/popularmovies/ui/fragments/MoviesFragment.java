package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.ui.DetailActivity;
import br.com.adalbertofjr.popularmovies.ui.SettingsActivity;
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

    private MoviesImageAdapter mMoviesAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gv_movies_fragment);

        mMoviesAdapter = new MoviesImageAdapter(getActivity(), new ArrayList<Movies>());
        gridView.setAdapter(mMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                startDetailMovie((Movies) adapter.getItemAtPosition(position));
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        startFetchMoviesTask();
    }

    private void startFetchMoviesTask() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    private void startDetailMovie(Movies movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.MOVIE_DETAIL_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, List<Movies>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movies> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonString;

            try {
                String optionSortFetchMovies = Util.getOptionSortFetchMovies(getActivity());
                URL url = new URL(optionSortFetchMovies);

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
        protected void onPostExecute(List<Movies> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                mMoviesAdapter.clear();
                mMoviesAdapter.addAll(movies);
            }
        }
    }

    private List<Movies> getMoviesDataFromJson(String moviesJsonString)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MOVIES_LIST_KEY);

        List<Movies> movies = new ArrayList<>();

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
