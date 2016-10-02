package br.com.adalbertofjr.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * PopularMovies
 * TestMoviesContract
 * <p>
 * Created by Adalberto Fernandes Júnior on 01/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */


public class TestMoviesContract extends AndroidTestCase {

    private static final String TEST_MOVIES_POPULAR = "/Captain America: Civil War";
    private static final String TEST_MOVIES_TOP_RATED = "/Captain America: Civil War";


    // Teste
    public void testBuildMoviesPopular() {
        Uri popularUri = MoviesContract.PopularEntry.buildMoviesPopular(TEST_MOVIES_POPULAR);

        assertNotNull("Error: Null Uri returned.  You must fill-in buildMoviesPopular in " +
                        "MoviesContract.",
                popularUri);

        assertEquals("Error: Movies popular not properly appended to the end of the Uri",
                TEST_MOVIES_POPULAR, popularUri.getLastPathSegment());

        assertEquals("Error: Movies popular Uri doesn't match our expected result",
                popularUri.toString(),
                "content://br.com.adalbertofjr.popularmovies/popular/%2FCaptain%20America%3A%20Civil%20War");
    }

    public void testBuildMoviesTopRated() {
        Uri topRatedUri = MoviesContract.TopRatedEntry.buildMoviesTopRated(TEST_MOVIES_TOP_RATED);

        assertNotNull("Error: Null Uri returned.  You must fill-in testBuildMoviesTopRated in " +
                        "MoviesContract.",
                topRatedUri);

        assertEquals("Error: Movies top_rated not properly appended to the end of the Uri",
                TEST_MOVIES_TOP_RATED, topRatedUri.getLastPathSegment());

        assertEquals("Error: Movies top_rated Uri doesn't match our expected result",
                topRatedUri.toString(),
                "content://br.com.adalbertofjr.popularmovies/top_rated/%2FCaptain%20America%3A%20Civil%20War");
    }
}
