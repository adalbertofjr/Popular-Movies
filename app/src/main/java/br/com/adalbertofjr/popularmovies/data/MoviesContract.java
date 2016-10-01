package br.com.adalbertofjr.popularmovies.data;

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

    private static final String PATH_POPULAR = "popular";


    public static final class PopularEntry implements BaseColumns {

        // Content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_POPULAR).build();

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
    }
}
