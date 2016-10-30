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

import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.model.Trailer;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * PopularMovies
 * FetchTrailersTask
 * <p>
 * Created by Adalberto Fernandes Júnior on 22/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class FetchTrailersTask extends AsyncTask<Movie, Void, Void> {
    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private Movie mMovie;
    private Context mContext;

    public FetchTrailersTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Movie... movie) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailersJsonString;

        try {

            mMovie = movie[0];

            if (mMovie == null)
                return null;

            String pathTrailer = String.format(Constants.MOVIE_TRAILERS_URL, mMovie.getId());

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

    private void getTrailersDataFromJson(String trailersJsonString)
            throws JSONException {

        JSONObject trailersJson = new JSONObject(trailersJsonString);
        JSONArray trailersArray = trailersJson.getJSONArray(Constants.TRAILERS_VIDEOS_LIST_KEY);

        for (int i = 0; i < trailersArray.length(); i++) {
            String id;
            String movieId;
            String key;
            String name;
            String site;

            JSONObject trailerData = trailersArray.getJSONObject(i);

            movieId = mMovie.getId();
            id = trailerData.getString(Constants.TRAILERS_VIDEO_ID);
            key = trailerData.getString(Constants.TRAILERS_VIDEO_KEY);
            name = trailerData.getString(Constants.TRAILERS_VIDEO_NAME);
            site = trailerData.getString(Constants.TRAILERS_VIDEO_SITE);

            Trailer trailer = new Trailer(
                    id,
                    movieId,
                    key,
                    name,
                    site
            );

            addTrailer(trailer);
        }
    }

    private long addTrailer(Trailer trailer) {
        long trailerId;
        Uri contentUri = MoviesContract.TrailersEntry.CONTENT_URI;

//        String[] projection = {MoviesContract.TrailersEntry._ID};
        String[] projection = null;
        String selection = MoviesContract.TrailersEntry._ID + " = ?";
        String[] selectionArgs = new String[]{trailer.getId()};

        Cursor cursor = mContext.getContentResolver().query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("_id");
            trailerId = cursor.getLong(idIndex);
        } else {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.TrailersEntry._ID, trailer.getId());
            values.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_ID, trailer.getIdMovie());
            values.put(MoviesContract.TrailersEntry.COLUMN_KEY, trailer.getKey());
            values.put(MoviesContract.TrailersEntry.COLUMN_NAME, trailer.getName());
            values.put(MoviesContract.TrailersEntry.COLUMN_SITE, trailer.getSite());

            Uri uri = mContext.getContentResolver().insert(contentUri,
                    values);

            trailerId = ContentUris.parseId(uri);
        }

        cursor.close();

        return trailerId;
    }
}
