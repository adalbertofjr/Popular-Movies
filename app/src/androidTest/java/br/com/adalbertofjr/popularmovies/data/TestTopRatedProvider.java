package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * PopularMovies
 * TestTopRatedProvider
 * <p>
 * Created by Adalberto Fernandes Júnior on 02/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestTopRatedProvider extends AndroidTestCase {
    private static final String LOG_TAG = TestTopRatedProvider.class.getSimpleName();

    /*
           This helper function deletes all records from both database tables using the ContentProvider.
           It also queries the ContentProvider to make sure that the database has been successfully
           deleted, so it cannot be used until the Query and Delete functions have been written
           in the ContentProvider.

           Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
           the delete functionality in the ContentProvider.
         */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MoviesContract.TopRatedEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.TopRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Popular table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
      This helper function deletes all records from both database tables using the database
      functions only.  This is designed to be used to reset the state of the database until the
      delete functionality is available in the ContentProvider.
    */
    public void deleteAllRecordsFromDB() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MoviesContract.TopRatedEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    /*
           This test doesn't touch the database.  It verifies that the ContentProvider returns
           the correct type for each type of URI that it can handle.
           Students: Uncomment this test to verify that your implementation of GetType is
           functioning correctly.
        */
    public void testGetType() {

        // content://br.com.adalbertofjr.popularmovies/top_rated/
        String type = mContext.getContentResolver().getType(MoviesContract.TopRatedEntry.CONTENT_URI);
        // vnd.android.cursor.dir/br.com.adalbertofjr.popularmovies/top_rated
        assertEquals("Error: the TopRatedEntry CONTENT_URI should return TopRatedEntry.CONTENT_TYPE",
                MoviesContract.TopRatedEntry.CONTENT_TYPE, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic popular query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicTopRatedQuery() {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Fantastic.  Now that we have add some movie!
        ContentValues movieValues = TestUtilities.createCaptainAmericaValues();

        long movieRowId = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert TopRatedEntry into the Database", movieRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor topRatedCursor = mContext.getContentResolver().query(
                MoviesContract.TopRatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicTopRatedQuery", topRatedCursor, movieValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
//    public void testInsertReadProvider() {
//        ContentValues testValues = TestUtilities.createCaptainAmericaValues();
//
//        // Register a content observer for our insert.  This time, directly with the content resolver
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.PopularEntry.CONTENT_URI, true, tco);
//        Uri popularUri = mContext.getContentResolver().insert(MoviesContract.PopularEntry.CONTENT_URI, testValues);
//
//        // Did our content observer get called?  Students:  If this fails, your insert popular
//        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        long locationRowId = ContentUris.parseId(popularUri);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                MoviesContract.PopularEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating PopularEntry.",
//                cursor, testValues);
//    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
//    public void testDeleteRecords() {
//        testInsertReadProvider();
//
//        // Register a content observer for our popular movie delete.
//        TestUtilities.TestContentObserver popularMovieObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(MoviesContract.PopularEntry.CONTENT_URI, true, popularMovieObserver);
//
//        deleteAllRecordsFromProvider();
//
//        // Students: If either of these fail, you most-likely are not calling the
//        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
//        // delete.  (only if the insertReadProvider is succeeding)
//        popularMovieObserver.waitForNotificationOrFail();
//
//        mContext.getContentResolver().unregisterContentObserver(popularMovieObserver);
//    }

    /*
       This test uses the provider to insert and then update the data. Uncomment this test to
       see if your update location is functioning correctly.
    */
//    public void testUpdatePopularMovie() {
//        ContentValues movieValues = TestUtilities.createCaptainAmericaValues();
//
//        Uri popularMovieUri = mContext.getContentResolver().
//                insert(MoviesContract.PopularEntry.CONTENT_URI, movieValues);
//
//        long popularMovieId = ContentUris.parseId(popularMovieUri);
//
//        assertTrue(popularMovieId != -1);
//        Log.d(LOG_TAG, "New row id: " + popularMovieId);
//
//        ContentValues updatedValues = new ContentValues(movieValues);
//        updatedValues.put(MoviesContract.PopularEntry.COLUMN_ORIGINAL_TITLE, "Capitão America");
//
//        Cursor popularMovieCursor = mContext.getContentResolver().query(MoviesContract.PopularEntry.CONTENT_URI, null, null, null, null);
//
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        popularMovieCursor.registerContentObserver(tco);
//
//        int count = mContext.getContentResolver().update(
//                MoviesContract.PopularEntry.CONTENT_URI, updatedValues, MoviesContract.PopularEntry._ID + "= ?",
//                new String[]{Long.toString(popularMovieId)});
//        assertEquals(count, 1);
//
//        tco.waitForNotificationOrFail();
//
//        popularMovieCursor.unregisterContentObserver(tco);
//        popularMovieCursor.close();
//
//        Cursor cursor = mContext.getContentResolver().query(
//                MoviesContract.PopularEntry.CONTENT_URI,
//                null,   // projection
//                MoviesContract.PopularEntry._ID + " = " + popularMovieId,
//                null,   // Values for the "where" clause
//                null    // sort order
//        );
//
//        TestUtilities.validateCursor("testUpdatePopularMovie.  Error validating location entry update.",
//                cursor, updatedValues);
//
//        cursor.close();
//    }
}
