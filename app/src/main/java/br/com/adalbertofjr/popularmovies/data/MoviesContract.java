package br.com.adalbertofjr.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Popular Movies
 * MoviesContract
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesContract {


    public static final class PopularEntry implements BaseColumns {

        // Popular table db
        public static final String TABLE_NAME = "popular";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";

    }
}
