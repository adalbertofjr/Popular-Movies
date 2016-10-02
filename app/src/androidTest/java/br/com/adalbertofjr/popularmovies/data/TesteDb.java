package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.TopRatedEntry;

/**
 * Popular Movies
 * TesteDb
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TesteDb extends AndroidTestCase {

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // tabelas
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(PopularEntry.TABLE_NAME);
        tableNameHashSet.add(TopRatedEntry.TABLE_NAME);

        this.mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);

        // instância do bd
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // verificando se conexão com banco está abertos
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain the tables entry´s
        assertTrue("Error: Your database was created without popular or top rated entry tables",
                tableNameHashSet.isEmpty());

        db.close();
    }

    public void testPopularColumns() {
        // instância do bd
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // verificando se conexão com banco está abertos
        assertEquals(true, db.isOpen());

        // now, do our tables contain the correct columns?
        Cursor c = db.rawQuery("PRAGMA table_info(" + PopularEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> popularColumnHashSet = new HashSet<>();
        popularColumnHashSet.add(PopularEntry._ID);
        popularColumnHashSet.add(PopularEntry.COLUMN_ORIGINAL_TITLE);
        popularColumnHashSet.add(PopularEntry.COLUMN_POSTER_PATH);
        popularColumnHashSet.add(PopularEntry.COLUMN_RELEASE_DATE);
        popularColumnHashSet.add(PopularEntry.COLUMN_VOTE_AVERAGE);
        popularColumnHashSet.add(PopularEntry.COLUMN_OVERVIEW);
        popularColumnHashSet.add(PopularEntry.COLUMN_BACKDROP_PATH);

        int columnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnNameIndex);
            popularColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required popular
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required popular entry columns",
                popularColumnHashSet.isEmpty());
        db.close();
    }

    public void testTopRatedColumns() {
        // instância do bd
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // verificando se conexão com banco está abertos
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("PRAGMA table_info(" + TopRatedEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> topRatedColumnHashSet = new HashSet<>();
        topRatedColumnHashSet.add(TopRatedEntry._ID);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_ORIGINAL_TITLE);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_POSTER_PATH);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_RELEASE_DATE);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_VOTE_AVERAGE);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_OVERVIEW);
        topRatedColumnHashSet.add(TopRatedEntry.COLUMN_BACKDROP_PATH);

        int columnNameIndex = c.getColumnIndex("name");// now, do our tables contain the correct columns?

        do {
            String columnName = c.getString(columnNameIndex);
            topRatedColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required popular
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required top_rated entry columns",
                topRatedColumnHashSet.isEmpty());
        db.close();
    }

    public void testPopularTable() {
        // Get reference database
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // Create values to insert
        ContentValues testValues = getContentValuesMovie();

        // Insert values into database and get row id get back
        db.insert(PopularEntry.TABLE_NAME, null, testValues);

        // Query the database and receive cursor back
        Cursor cursor = db.query(PopularEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",
                cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Popular Query Validation Failed",
                cursor, testValues);


        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    public void testTopRatedTable() {
        // Get reference database
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // Create values to insert
        ContentValues testValues = getContentValuesMovie();

        // Insert values into database and get row id get back
        db.insert(TopRatedEntry.TABLE_NAME, null, testValues);

        // Query the database and receive cursor back
        Cursor cursor = db.query(TopRatedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",
                cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Top Rated Query Validation Failed",
                cursor, testValues);


        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    private ContentValues getContentValuesMovie() {
        ContentValues testValues = new ContentValues();
        testValues.put(PopularEntry._ID, 271110);
        testValues.put(PopularEntry.COLUMN_ORIGINAL_TITLE, "Captain America: Civil War");
        testValues.put(PopularEntry.COLUMN_POSTER_PATH, "/5N20rQURev5CNDcMjHVUZhpoCNC.jpg");
        testValues.put(PopularEntry.COLUMN_RELEASE_DATE, "2016-04-27");
        testValues.put(PopularEntry.COLUMN_VOTE_AVERAGE, 6.94);
        testValues.put(PopularEntry.COLUMN_OVERVIEW, "Matrix");
        testValues.put(PopularEntry.COLUMN_BACKDROP_PATH, "/rqAHkvXldb9tHlnbQDwOzRi0yVD.jpg");
        return testValues;
    }
}
