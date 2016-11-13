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
 * FetchReviewsService
 * <p>
 * Created by Adalberto Fernandes Júnior on 13/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class FetchReviewsService extends IntentService {
    private static final String LOG_TAG = FetchReviewsService.class.getSimpleName();
    private String mMovieId;

    public FetchReviewsService(String name) {
        super(name);
    }

    public FetchReviewsService() {
        super("FetchReviewsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewsJsonString;

        try {
            mMovieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (mMovieId == null)
                return;

            String pathReview = String.format(Constants.MOVIE_REVIEWS_URL, mMovieId);

            URL url = new URL(pathReview);

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

            reviewsJsonString = buffer.toString();

            try {
                getReviewsDataFromJson(reviewsJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
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

    private void getReviewsDataFromJson(String reviewsJsonString)
            throws JSONException {

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);
        JSONArray reviewsArray = reviewsJson.getJSONArray(Constants.REVIEWS_VIDEOS_LIST_KEY);

        // Insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<>(reviewsArray.length());

        for (int i = 0; i < reviewsArray.length(); i++) {
            String id;
            String idMovie;
            String name;
            String content;

            JSONObject reviewData = reviewsArray.getJSONObject(i);

            idMovie = mMovieId;
            id = reviewData.getString(Constants.REVIEWS_VIDEO_ID);
            name = reviewData.getString(Constants.REVIEWS_VIDEOS_AUTHOR);
            content = reviewData.getString(Constants.REVIEWS_VIDEOS_CONTENT);

            ContentValues reviewsValues = new ContentValues();
            reviewsValues.put(MoviesContract.ReviewsEntry._ID, id);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID, idMovie);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, name);
            reviewsValues.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT, content);

            cVVector.add(reviewsValues);
        }

        bulkInsertReviews(cVVector);
    }

    private void bulkInsertReviews(Vector<ContentValues> cVVector) {
        Uri contentUri = MoviesContract.ReviewsEntry.CONTENT_URI;

        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(contentUri, cvArray);
            Log.d(LOG_TAG, "Reviews " + cVVector.size() + " inserted.");
        }

    }
}
