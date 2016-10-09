package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Popular Movies
 * MoviesContract
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "br.com.adalbertofjr.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_TRAILERS = "trailers";


    public static final class PopularEntry implements BaseColumns {

        // Content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        // Popular table db
        public static final String TABLE_NAME = "popular";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildMoviesPopular(String testMoviesPopular) {
            return CONTENT_URI.buildUpon().appendPath(testMoviesPopular).build();
        }

        public static Uri buildPopularMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TopRatedEntry implements BaseColumns {
        // Content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_TOP_RATED).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;

        // Top Rated table db
        public static final String TABLE_NAME = "top_rated";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildMoviesTopRated(String testMoviesTopRated) {
            return CONTENT_URI.buildUpon().appendPath(testMoviesTopRated).build();
        }

        public static Uri buildTopRatedMovieUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TrailersEntry implements BaseColumns {
        // Content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        //Trailer table db
        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";

        public static Uri buildMoviesTrailers(String testMoviesTrailers) {
            return CONTENT_URI.buildUpon().appendPath(testMoviesTrailers).build();
        }
    }
}
