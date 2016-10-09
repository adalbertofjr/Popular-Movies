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
    private static final String TEST_MOVIES_TRAILERS = "/271110";
    private static final String TEST_MOVIES_REVIEWS = "/271110";


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

    public void testBuildMoviesTrailers() {
        Uri trailersUri = MoviesContract.TrailersEntry.buildMoviesTrailers(TEST_MOVIES_TRAILERS);

        assertNotNull("Error: Null Uri returned.  You must fill-in testBuildMoviesTrailers in " +
                        "MoviesContract.",
                trailersUri);

        assertEquals("Error: Movies trailers not properly appended to the end of the Uri",
                TEST_MOVIES_TRAILERS, trailersUri.getLastPathSegment());

        assertEquals("Error: Movies trailers Uri doesn't match our expected result",
                trailersUri.toString(),
                "content://br.com.adalbertofjr.popularmovies/trailers/%2F271110");
    }

    public void testBuildMoviesReviews() {
        Uri reviewsUri = MoviesContract.ReviewsEntry.buildMoviesReviews(TEST_MOVIES_REVIEWS);

        assertNotNull("Error: Null Uri returned.  You must fill-in testBuildMoviesReviews in " +
                        "MoviesContract.",
                reviewsUri);

        assertEquals("Error: Movies reviews not properly appended to the end of the Uri",
                TEST_MOVIES_REVIEWS, reviewsUri.getLastPathSegment());

        assertEquals("Error: Movies reviews Uri doesn't match our expected result",
                reviewsUri.toString(),
                "content://br.com.adalbertofjr.popularmovies/reviews/%2F271110");
    }
}
