package br.com.adalbertofjr.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Vector;

import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * PopularMovies
 * FetchTrailersService
 * <p>
 * Created by Adalberto Fernandes Júnior on 13/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class FetchTrailersService extends IntentService {
    private String LOG_TAG = FetchTrailersService.class.getSimpleName();
    private String mMovie;

    public FetchTrailersService(String name) {
        super(name);
    }

    public FetchTrailersService() {
        super("FetchTrailersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailersJsonString;

        try {

            mMovie = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (mMovie == null)
                return;

            String pathTrailer = String.format(Constants.MOVIE_TRAILERS_URL, mMovie);

            URL url = new URL(pathTrailer);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            trailersJsonString = buffer.toString();

            try {
                getTrailersDataFromJson(trailersJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return;
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
    }

    private void getTrailersDataFromJson(String trailersJsonString)
            throws JSONException {

        JSONObject trailersJson = new JSONObject(trailersJsonString);
        JSONArray trailersArray = trailersJson.getJSONArray(Constants.TRAILERS_VIDEOS_LIST_KEY);

        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<>(trailersArray.length());

        for (int i = 0; i < trailersArray.length(); i++) {
            String id;
            String movieId;
            String key;
            String name;
            String site;

            JSONObject trailerData = trailersArray.getJSONObject(i);

            movieId = mMovie;
            id = trailerData.getString(Constants.TRAILERS_VIDEO_ID);
            key = trailerData.getString(Constants.TRAILERS_VIDEO_KEY);
            name = trailerData.getString(Constants.TRAILERS_VIDEO_NAME);
            site = trailerData.getString(Constants.TRAILERS_VIDEO_SITE);

            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MoviesContract.TrailersEntry._ID, id);
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_ID, movieId);
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_KEY, key);
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_NAME, name);
            trailerValues.put(MoviesContract.TrailersEntry.COLUMN_SITE, site);

            cVVector.add(trailerValues);
        }

        bulkInsertTrailers(cVVector);
    }

    private void bulkInsertTrailers(Vector<ContentValues> cVVector) {
        Uri contentUri = MoviesContract.TrailersEntry.CONTENT_URI;

        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(contentUri, cvArray);
            Log.d(LOG_TAG, "Trailers " + cVVector.size() + " inserted.");
        }

    }
}
