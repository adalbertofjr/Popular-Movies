package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;

/**
 * Popular Movies
 * TesteDb
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TesteDb extends AndroidTestCase {

    @Override
    protected void setUp() {
        deleteTheDatabase();
    }

    private void deleteTheDatabase() {
        mContext.deleteDatabase(PopularEntry.TABLE_NAME);
    }

    public void testCreateDb() throws Throwable {
        // tabelas
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(PopularEntry.TABLE_NAME);

        // apagando tabela
        mContext.deleteDatabase(PopularEntry.TABLE_NAME);

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

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + PopularEntry.TABLE_NAME + ")",
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

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                popularColumnHashSet.isEmpty());
        db.close();
    }

    public void testPopularTable() {
        // Get reference database
        SQLiteDatabase db = new MoviesDbHelper(this.mContext).getWritableDatabase();

        // Create values to insert
        ContentValues testValues = getContentValuesPopular();

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

    private ContentValues getContentValuesPopular() {
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
