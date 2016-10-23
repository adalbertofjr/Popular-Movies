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
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.model.Review;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * PopularMovies
 * FetchReviewsTask
 * <p>
 * Created by Adalberto Fernandes Júnior on 23/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class FetchReviewsTask extends AsyncTask<Movie, Void, Void> {
    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private Movie mMovie;
    private Context mContext;

    public FetchReviewsTask(Context mContext) {
        this.mMovie = mMovie;
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Movie... movies) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String reviewsJsonString;

        try {
            mMovie = movies[0];

            if (mMovie == null)
                return null;

            String pathReview = String.format(Constants.MOVIE_REVIEWS_URL, mMovie.getId());

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
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            reviewsJsonString = buffer.toString();

            try {
                getReviewsDataFromJson(reviewsJsonString);
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

    private void getReviewsDataFromJson(String reviewsJsonString)
            throws JSONException {

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);
        JSONArray reviewsArray = reviewsJson.getJSONArray(Constants.REVIEWS_VIDEOS_LIST_KEY);

        ArrayList<Review> reviews = new ArrayList<>();

        for (int i = 0; i < reviewsArray.length(); i++) {
            String id;
            String idMovie;
            String name;
            String content;

            JSONObject reviewData = reviewsArray.getJSONObject(i);

            idMovie = mMovie.getId();
            id = reviewData.getString(Constants.REVIEWS_VIDEO_ID);
            name = reviewData.getString(Constants.REVIEWS_VIDEOS_AUTHOR);
            content = reviewData.getString(Constants.REVIEWS_VIDEOS_CONTENT);

            Review review = new Review(
                    id,
                    idMovie,
                    name,
                    content
            );

            addReview(review);
        }
    }

    private long addReview(Review review) {
        long reviewId;
        Uri contentUri = MoviesContract.ReviewsEntry.CONTENT_URI;

        String[] projection = {MoviesContract.TrailersEntry._ID};
        String selection = MoviesContract.TrailersEntry._ID + " = ?";
        String[] selectionArgs = new String[]{review.getId()};

        Cursor cursor = mContext.getContentResolver().query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("_id");
            reviewId = cursor.getLong(idIndex);
        } else {

            ContentValues values = new ContentValues();
            values.put(MoviesContract.ReviewsEntry._ID, review.getId());
            values.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID, review.getIdMovie());
            values.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, review.getAuthor());
            values.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT, review.getContent());

            Uri uri = mContext.getContentResolver().insert(contentUri,
                    values);

            reviewId = ContentUris.parseId(uri);
        }

        cursor.close();

        return reviewId;
    }
}