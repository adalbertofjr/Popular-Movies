package br.com.adalbertofjr.popularmovies.tasks;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.test.AndroidTestCase;

import br.com.adalbertofjr.popularmovies.data.MoviesContract;

/**
 * PopularMovies
 * TestFetchPopularMoviesTask
 * <p>
 * Created by Adalberto Fernandes Júnior on 09/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestFetchMoviesTask extends AndroidTestCase {
    private static final String ADD_ID = "271110";
    private static final String ADD_BACKDROP_PATH = "/rqAHkvXldb9tHlnbQDwOzRi0yVD.jpg";
    private static final String ADD_POSTER_PATH = "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg";
    private static final String ADD_VOTE_AVERAGE = "6.94";
    private static final String ADD_ORIGINAL_TITLE = "Captain America: Civil War";
    private static final String ADD_RELEASE_DATE = "2016-04-27";
    private static final String ADD_OVERVIEW = "Following the events of Age of Ultron, the collective governments of the world pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, causing two factions to side with Iron Man or Captain America, which causes an epic battle between former allies.";

    @TargetApi(11)
    public void testAddMovie() {
        // start from a clean state
        getContext().getContentResolver().delete(MoviesContract.PopularEntry.CONTENT_URI,
                MoviesContract.PopularEntry._ID + " = ?",
                new String[]{ADD_ID});

        FetchMoviesTask fwt = new FetchMoviesTask(getContext());
        long movieId = fwt.addMovie(ADD_ID, ADD_BACKDROP_PATH,
                ADD_POSTER_PATH, ADD_VOTE_AVERAGE, ADD_ORIGINAL_TITLE,
                ADD_RELEASE_DATE, ADD_OVERVIEW);

        // does addLocation return a valid record ID?
        assertFalse("Error: addMovie returned an invalid ID on insert",
                movieId == -1);

        // test all this twice
        for (int i = 0; i < 2; i++) {

            // does the ID point to our location?
            Cursor cursor = getContext().getContentResolver().query(
                    MoviesContract.PopularEntry.CONTENT_URI,
                    new String[]{
                            MoviesContract.PopularEntry._ID,
                            MoviesContract.PopularEntry.COLUMN_ORIGINAL_TITLE,
                            MoviesContract.PopularEntry.COLUMN_POSTER_PATH,
                            MoviesContract.PopularEntry.COLUMN_RELEASE_DATE,
                            MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE,
                            MoviesContract.PopularEntry.COLUMN_OVERVIEW,
                            MoviesContract.PopularEntry.COLUMN_BACKDROP_PATH
                    },
                    MoviesContract.PopularEntry._ID + " = ?",
                    new String[]{ADD_ID},
                    null);


            // these match the indices of the projection
            if (cursor.moveToFirst()) {
                assertEquals("Error: the queried value of movieId does not match the returned value" +
                        "from addLocation", cursor.getLong(0), movieId);
                assertEquals("Error: the queried value of title setting is incorrect",
                        cursor.getString(1), ADD_ORIGINAL_TITLE);
                assertEquals("Error: the queried value of poster city is incorrect",
                        cursor.getString(2), ADD_POSTER_PATH);
                assertEquals("Error: the queried value of release date is incorrect",
                         cursor.getString(3), ADD_RELEASE_DATE);
                assertEquals("Error: the queried value of vote average is incorrect",
                        String.valueOf(cursor.getDouble(4)), ADD_VOTE_AVERAGE);
                assertEquals("Error: the queried value of overview is incorrect",
                        cursor.getString(5), ADD_OVERVIEW);
                assertEquals("Error: the queried value of backdrop path is incorrect",
                        cursor.getString(6), ADD_BACKDROP_PATH);

            } else {
                fail("Error: the id you used to query returned an empty cursor");
            }

            // there should be no more records
            assertFalse("Error: there should be only one record returned from a location query",
                    cursor.moveToNext());

            // add the location again
            long newLocationId = fwt.addMovie(ADD_ID, ADD_BACKDROP_PATH,
                    ADD_POSTER_PATH, ADD_VOTE_AVERAGE, ADD_ORIGINAL_TITLE,
                    ADD_RELEASE_DATE, ADD_OVERVIEW);

            assertEquals("Error: inserting a location again should return the same ID",
                    movieId, newLocationId);
        }
        // reset our state back to normal
        getContext().getContentResolver().delete(MoviesContract.PopularEntry.CONTENT_URI,
                MoviesContract.PopularEntry._ID + " = ?",
                new String[]{ADD_ID});

        // clean up the test so that other tests can use the content provider
        getContext().getContentResolver().
                acquireContentProviderClient(MoviesContract.PopularEntry.CONTENT_URI).
                getLocalContentProvider().shutdown();
    }
}
