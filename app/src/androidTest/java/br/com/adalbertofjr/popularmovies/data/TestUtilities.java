package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public static ContentValues createCaptainAmericaPopularValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MoviesContract.PopularEntry._ID, 271110);
        testValues.put(MoviesContract.PopularEntry.COLUMN_ORIGINAL_TITLE, "Captain America: Civil War");
        testValues.put(MoviesContract.PopularEntry.COLUMN_POSTER_PATH, "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg");
        testValues.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE, "2016-04-27");
        testValues.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, 6.94);
        testValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, "Matrix");
        testValues.put(MoviesContract.PopularEntry.COLUMN_BACKDROP_PATH, "/rqAHkvXldb9tHlnbQDwOzRi0yVD.jpg");
        return testValues;
    }

    public static long insertCaptainAmericaPopularValues(Context context) {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCaptainAmericaPopularValues();

        long locationRowId;
        locationRowId = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Captain America Popular Values", locationRowId != -1);

        return locationRowId;
    }

    public static ContentValues createMovieValues() {
        return null;
    }
}
