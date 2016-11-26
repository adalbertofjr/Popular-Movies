package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * PopularMovies
 * TestFavoritesProvider
 * <p>
 * Created by Adalberto Fernandes Júnior on 26/11/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestFavoritesProvider extends AndroidTestCase {
    private static final String LOG_TAG = TestFavoritesProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Popular table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MoviesContract.FavoritesEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    public void testGetType() {

        // content://br.com.adalbertofjr.popularmovies/favorites/
        String type = mContext.getContentResolver().getType(MoviesContract.FavoritesEntry.CONTENT_URI);
        // vnd.android.cursor.dir/br.com.adalbertofjr.popularmovies/favorites
        assertEquals("Error: the FavoritesEntry CONTENT_URI should return FavoritesEntry.CONTENT_TYPE",
                MoviesContract.FavoritesEntry.CONTENT_TYPE, type);

        long idMovie = 43074L; // December 21st, 2014
        // content://br.com.adalbertofjr.popularmovies/favorites/43074
        type = mContext.getContentResolver().getType(
                MoviesContract.FavoritesEntry.buildFavoritesMovieUri(idMovie));
        // vnd.android.cursor.item/br.com.adalbertofjr.popularmovies/favorites/43074
        assertEquals("Error: the FavoritesEntry CONTENT_URI with movie id should return FavoritesEntry.CONTENT_ITEM_TYPE",
                MoviesContract.FavoritesEntry.CONTENT_ITEM_TYPE, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic favorites query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicFavoritesQuery() {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Fantastic.  Now that we have add some movie!
        ContentValues movieValues = TestUtilities.createCaptainAmericaValues();

        long rowId = db.insert(MoviesContract.FavoritesEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert FavoritesEntry into the Database", rowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicFavoritesQuery", cursor, movieValues);

        Uri uriWithId = MoviesContract.FavoritesEntry.buildFavoritesMovieUri(271110);

        Cursor favoriteWithMovieCursor = mContext.getContentResolver().query(
                uriWithId,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicFavoritesQueryWithMovie", favoriteWithMovieCursor, movieValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadFavoritesProvider() {
        ContentValues testValues = TestUtilities.createCaptainAmericaValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.FavoritesEntry.CONTENT_URI, true, tco);
        Uri favoritesUri = mContext.getContentResolver().insert(MoviesContract.FavoritesEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert favorites
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(favoritesUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadFavoritesProvider. Error validating FavoritesEntry.",
                cursor, testValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteFavoritesRecords() {
        testInsertReadFavoritesProvider();

        // Register a content observer for our favorites movie delete.
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.FavoritesEntry.CONTENT_URI, true, tco);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        tco.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(tco);
    }

    /*
       This test uses the provider to insert and then update the data. Uncomment this test to
       see if your update location is functioning correctly.
    */
    public void testUpdateFavoritesMovie() {
        ContentValues movieValues = TestUtilities.createCaptainAmericaValues();

        Uri popularMovieUri = mContext.getContentResolver().
                insert(MoviesContract.FavoritesEntry.CONTENT_URI, movieValues);

        long movieId = ContentUris.parseId(popularMovieUri);

        assertTrue(movieId != -1);
        Log.d(LOG_TAG, "New row id: " + movieId);

        ContentValues updatedValues = new ContentValues(movieValues);
        updatedValues.put(MoviesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE, "Capitão America");

        Cursor favoritesMovieCursor = mContext.getContentResolver().query(MoviesContract.FavoritesEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        favoritesMovieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MoviesContract.FavoritesEntry.CONTENT_URI, updatedValues, MoviesContract.FavoritesEntry._ID + "= ?",
                new String[]{Long.toString(movieId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();

        favoritesMovieCursor.unregisterContentObserver(tco);
        favoritesMovieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                null,   // projection
                MoviesContract.FavoritesEntry._ID + " = " + movieId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateFavoritesMovie.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }
}
