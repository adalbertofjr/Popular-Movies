package br.com.adalbertofjr.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.tasks.FetchReviewsTask;
import br.com.adalbertofjr.popularmovies.tasks.FetchTrailersTask;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * PopularMovies
 * PopularMoviesSyncAdapter
 * <p>
 * Created by Adalberto Fernandes Júnior on 06/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class PopularMoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = PopularMoviesSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int POPULAR_NOTIFICATION_ID = 3004;


    public PopularMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        getMoviesDataFromApi(Constants.MOVIES_POPULAR_PATH);
        getMoviesDataFromApi(Constants.MOVIES_TOP_RATED_PATH);
    }

    private void getMoviesDataFromApi(String path) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonString;

        try {
            if (path == null) {
                return;
            }

            String uriOption;
            if (path.equals(Constants.MOVIES_POPULAR_PATH)) {
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
                return;
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
                return;
            }

            moviesJsonString = buffer.toString();

            try {
                getMoviesDataFromJson(path, moviesJsonString);
                return;
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

    private void getMoviesDataFromJson(String path, String moviesJsonString)
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

            Movie movie = new Movie(id, backdrop_path,
                    poster_path,
                    vote_average,
                    original_title,
                    release_date,
                    overview);

            addMovie(path, movie.getId(), movie.getBackDropUrlPath(), movie.getPoster_path(),
                    movie.getVote_average(), movie.getOriginal_title(), movie.getRelease_date(),
                    movie.getOverview());
        }

    }


    private long addMovie(String path, String id, String backdropPath, String posterPath, String voteAverage,
                          String originalTitle, String releaseDate, String overview) {
        long movieId;

        String[] projection = {MoviesContract.PopularEntry._ID};
        String selection = MoviesContract.PopularEntry._ID + " = ?";
        String[] selectionArgs = new String[]{id};

        Uri contentUri;

        if (path.equals(Constants.MOVIES_POPULAR_PATH)) {
            contentUri = MoviesContract.PopularEntry.CONTENT_URI;
        } else {
            contentUri = MoviesContract.TopRatedEntry.CONTENT_URI;
        }

        Cursor cursor = getContext().getContentResolver().query(
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

            Uri uri = getContext().getContentResolver().insert(contentUri,
                    values);

            movieId = ContentUris.parseId(uri);

            // Buscando trailers e reviews
            Movie movie = new Movie();
            movie.setId(Long.toString(movieId));
            new FetchTrailersTask(getContext()).execute(movie);
            new FetchReviewsTask(getContext()).execute(movie);
        }

        cursor.close();

        return movieId;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PopularMoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
