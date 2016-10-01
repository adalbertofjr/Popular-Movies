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


    // Teste
    public void testBuildMoviesPopular(){
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
}
