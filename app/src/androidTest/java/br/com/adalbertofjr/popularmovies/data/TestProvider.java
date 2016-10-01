package br.com.adalbertofjr.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;

/**
 * PopularMovies
 * TestProvider
 * <p>
 * Created by Adalberto Fernandes Júnior on 01/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestProvider extends AndroidTestCase {

    /*
      This helper function deletes all records from both database tables using the database
      functions only.  This is designed to be used to reset the state of the database until the
      delete functionality is available in the ContentProvider.
    */
    public void deleteAllRecordsFromDB() {
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(PopularEntry.TABLE_NAME, null, null);
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
            This test checks to make sure that the content provider is registered correctly.
            Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
         */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MoviesProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MoviesProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MoviesProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
           This test doesn't touch the database.  It verifies that the ContentProvider returns
           the correct type for each type of URI that it can handle.
           Students: Uncomment this test to verify that your implementation of GetType is
           functioning correctly.
        */
    public void testGetType() {

        // content://br.com.adalbertofjr.popularmovies/popular/
        String type = mContext.getContentResolver().getType(PopularEntry.CONTENT_URI);
        // vnd.android.cursor.dir/br.com.adalbertofjr.popularmovies/popular
        assertEquals("Error: the PopularEntry CONTENT_URI should return PopularEntry.CONTENT_TYPE",
                PopularEntry.CONTENT_TYPE, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic popular query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicPopularQuery() {
        // insert our test records into the database
        MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Fantastic.  Now that we have add some movie!
        ContentValues movieValues = TestUtilities.createCaptainAmericaPopularValues();;

        long weatherRowId = db.insert(PopularEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert PopularEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor popularCursor = mContext.getContentResolver().query(
                PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicPopularQuery", popularCursor, movieValues);
    }
}
