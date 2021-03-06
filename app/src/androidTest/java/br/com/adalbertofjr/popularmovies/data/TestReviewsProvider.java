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
 * TestReviewsProvider
 * <p>
 * Created by Adalberto Fernandes Júnior on 09/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestReviewsProvider extends AndroidTestCase {
    private static final String LOG_TAG = TestReviewsProvider.class.getSimpleName();

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
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null,
                null
        );


        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from reviews table during delete", 0, cursor.getCount());
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

        db.delete(MoviesContract.ReviewsEntry.TABLE_NAME, null, null);
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

        // content://br.com.adalbertofjr.popularmovies/reviews/
        String type = mContext.getContentResolver().getType(MoviesContract.ReviewsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/br.com.adalbertofjr.popularmovies/reviews
        assertEquals("Error: the ReviewsEntry CONTENT_URI should return ReviewsEntry.CONTENT_TYPE",
                MoviesContract.ReviewsEntry.CONTENT_TYPE, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic popular query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicReviewsQuery() {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Fantastic.  Now that we have add some trailer!
        ContentValues reviewValues = TestUtilities.createReviewsValues();

        long movieRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, reviewValues);
        assertTrue("Unable to insert ReviewsEntry into the Database", movieRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor reviewsCursor = mContext.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicReviewsQuery", reviewsCursor, reviewValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createReviewsValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.ReviewsEntry.CONTENT_URI, true, tco);
        Uri trailersUri = mContext.getContentResolver().insert(MoviesContract.ReviewsEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert popular
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long reviewsRowId = ContentUris.parseId(trailersUri);

        // Verify we got a row back.
        assertTrue(reviewsRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ReviewsEntry.",
                cursor, testValues);
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our popular movie delete.
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MoviesContract.ReviewsEntry.CONTENT_URI, true, tco);

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
    public void testUpdateReviewsMovie() {
        ContentValues trailerValues = TestUtilities.createReviewsValues();

        Uri reviewMovieUri = mContext.getContentResolver().
                insert(MoviesContract.ReviewsEntry.CONTENT_URI, trailerValues);

        long reviewMovieId = ContentUris.parseId(reviewMovieUri);

        assertTrue(reviewMovieId != -1);
        Log.d(LOG_TAG, "New row id: " + reviewMovieId);

        ContentValues updatedValues = new ContentValues(trailerValues);
        updatedValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR, "SADOVSKI");

        Cursor trailerMovieCursor = mContext.getContentResolver().query(MoviesContract.ReviewsEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        trailerMovieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MoviesContract.ReviewsEntry.CONTENT_URI, updatedValues, MoviesContract.ReviewsEntry._ID + "= ?",
                new String[]{Long.toString(reviewMovieId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();

        trailerMovieCursor.unregisterContentObserver(tco);
        trailerMovieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null,   // projection
                MoviesContract.ReviewsEntry._ID + " = " + reviewMovieId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateReviewsMovie.  Error validating reviews entry update.",
                cursor, updatedValues);

        cursor.close();
    }
}
