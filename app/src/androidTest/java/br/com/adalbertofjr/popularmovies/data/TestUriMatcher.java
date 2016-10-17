package br.com.adalbertofjr.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * PopularMovies
 * TestUriMatcher
 * <p>
 * Created by Adalberto Fernandes Júnior on 15/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestUriMatcher extends AndroidTestCase {
    private static final long MOVIE_QUERY_ID= 47933L;

    private static final Uri TEST_POPULAR_DIR = MoviesContract.PopularEntry.CONTENT_URI;
    private static final Uri TEST_POPULAR_WITH_MOVIE_DIR = MoviesContract.PopularEntry.buildPopularMovieUri(MOVIE_QUERY_ID);
    private static final Uri TEST_TOP_RATED_DIR = MoviesContract.TopRatedEntry.CONTENT_URI;
    private static final Uri TEST_TOP_RATED_WITH_MOVIE_DIR = MoviesContract.TopRatedEntry.buildTopRatedMovieUri(MOVIE_QUERY_ID);

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MoviesProvider.buildUriMatcher();

        assertEquals("Error: The POPULAR URI was matched incorrectly.",
                testMatcher.match(TEST_POPULAR_DIR), MoviesProvider.POPULAR);
        assertEquals("Error: The POPULAR WITH MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_POPULAR_WITH_MOVIE_DIR), MoviesProvider.POPULAR_WITH_MOVIE);

        assertEquals("Error: The TOP RATED URI was matched incorrectly.",
                testMatcher.match(TEST_TOP_RATED_DIR), MoviesProvider.TOP_RATED);

        assertEquals("Error: The TOP RATED WITH MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_TOP_RATED_WITH_MOVIE_DIR), MoviesProvider.TOP_RATED_WITH_MOVIE);
    }
}
