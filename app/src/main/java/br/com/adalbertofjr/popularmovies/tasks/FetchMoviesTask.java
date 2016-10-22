package br.com.adalbertofjr.popularmovies.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * PopularMovies
 * FetchMoviesTask
 * <p>
 * Created by Adalberto Fernandes Júnior on 09/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movies>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final Context mContext;
    private String mFetchOption;

    public FetchMoviesTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<Movies> doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonString;

        try {
            mFetchOption = strings[0];

            if (mFetchOption == null) {
                return null;
            }

            String uriOption;
            if (mFetchOption.equals(Constants.MOVIES_POPULAR_PATH)) {
                uriOption = Constants.MOVIES_POPULAR_URL;
            } else {
                uriOption = Constants.MOVIES_TOP_RATED_URL;
            }

            URL url = new URL(uriOption);

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
                getMoviesDataFromJson(moviesJsonString);
                return null;
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

    private void getMoviesDataFromJson(String moviesJsonString)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MOVIES_LIST_KEY);

        for (int i = 0; i < moviesArray.length(); i++) {
            String id;
            String backdrop_path;
            String poster_path;
            String vote_average;
            String original_title;
            String release_date;
            String overview;

            JSONObject movieData = moviesArray.getJSONObject(i);

            id = movieData.getString(Constants.MOVIES_ID);
            backdrop_path = movieData.getString(Constants.MOVIES_BACKGROUND_KEY);
            poster_path = movieData.getString(Constants.MOVIES_POSTER_KEY);
            vote_average = movieData.getString(Constants.MOVIES_VOTE_AVERAGE_KEY);
            original_title = movieData.getString(Constants.MOVIES_TITLE_KEY);
            release_date = movieData.getString(Constants.MOVIES_RELEASE_DATE_KEY);
            overview = movieData.getString(Constants.MOVIES_OVERVIEW_KEY);

            Movies movie = new Movies(id, backdrop_path,
                    poster_path,
                    vote_average,
                    original_title,
                    release_date,
                    overview);

            addMovie(movie.getId(), movie.getBackDropUrlPath(), movie.getPoster_path(),
                    movie.getVote_average(), movie.getOriginal_title(), movie.getRelease_date(),
                    movie.getOverview());
        }

    }


    private long addMovie(String id, String backdropPath, String posterPath, String voteAverage,
                         String originalTitle, String releaseDate, String overview) {
        long movieId;

        String[] projection = {MoviesContract.PopularEntry._ID};
        String selection = MoviesContract.PopularEntry._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Uri contentUri;

        if (mFetchOption.equals(Constants.MOVIES_POPULAR_PATH)) {
            contentUri = MoviesContract.PopularEntry.CONTENT_URI;
        } else {
            contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
        }

        Cursor cursor = mContext.getContentResolver().query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("_id");
            movieId = cursor.getLong(idIndex);
        } else {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.PopularEntry._ID, id);
            values.put(MoviesContract.PopularEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
            values.put(MoviesContract.PopularEntry.COLUMN_POSTER_PATH, posterPath);
            values.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE, releaseDate);
            values.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, voteAverage);
            values.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, overview);
            values.put(MoviesContract.PopularEntry.COLUMN_BACKDROP_PATH, backdropPath);

            Uri uri = mContext.getContentResolver().insert(contentUri,
                    values);

            movieId = ContentUris.parseId(uri);

            // Buscando trailers e reviews
            Movies movie = new Movies();
            movie.setId(Long.toString(movieId));
            new FetchTrailersTask(mContext).execute(movie);
        }

        cursor.close();

        return movieId;
    }
}