package br.com.adalbertofjr.popularmovies.util;

import br.com.adalbertofjr.popularmovies.BuildConfig;

/**
 * Popular Movies
 * Constants
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Constants {
    //EXTRA
    public static final String MOVIE_DETAIL_EXTRA = "movie";

    //Key for api The Movie Db
    public static String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_KEY;

    //The Movie DB paths
    public static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String MOVIE_IMAGE_W500 = "w500/";
    public static final String MOVIE_IMAGE_W185 = "w185/";
    public static final String MOVIE_IMAGE_BACKDROP_PATH_URL = Constants.MOVIE_IMAGE_BASE_URL + Constants.MOVIE_IMAGE_W500;
    public static final String MOVIE_IMAGE_POSTER_PATH_URL = Constants.MOVIE_IMAGE_BASE_URL + Constants.MOVIE_IMAGE_W185;
}
