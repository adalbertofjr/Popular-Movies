package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Popular Movies
 * TestUtilities
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }


    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static ContentValues createCaptainAmericaValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.PopularEntry._ID, 271110);
        testValues.put(MoviesContract.PopularEntry.COLUMN_ORIGINAL_TITLE, "Captain America: Civil War");
        testValues.put(MoviesContract.PopularEntry.COLUMN_POSTER_PATH, "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg");
        testValues.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE, "2016-04-27");
        testValues.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, 6.94);
        testValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, "Following the events of Age of Ultron, the collective governments of the world pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, causing two factions to side with Iron Man or Captain America, which causes an epic battle between former allies.");
        testValues.put(MoviesContract.PopularEntry.COLUMN_BACKDROP_PATH, "/rqAHkvXldb9tHlnbQDwOzRi0yVD.jpg");
        return testValues;
    }

    public static ContentValues createTrailerValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.TrailersEntry._ID, 271110);
        testValues.put(MoviesContract.TrailersEntry.COLUMN_KEY, "43NWzay3W4s");
        testValues.put(MoviesContract.TrailersEntry.COLUMN_NAME, "Official Trailer #1");
        testValues.put(MoviesContract.TrailersEntry.COLUMN_SITE, "YouTube");
        return testValues;
    }

    public static long insertCaptainAmericaPopularValues(Context context) {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCaptainAmericaValues();

        long locationRowId;
        locationRowId = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Captain America Popular Values", locationRowId != -1);

        return locationRowId;
    }

    /*
     Students: The functions we provide inside of TestProvider use this utility class to test
             the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
             CTS tests.

     Note that this only tests that the onChange function is called; it does not test that the
     correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, android.net.Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new br.com.adalbertofjr.popularmovies.util.PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
