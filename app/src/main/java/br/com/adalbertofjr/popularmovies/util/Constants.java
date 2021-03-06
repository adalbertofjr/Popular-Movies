package br.com.adalbertofjr.popularmovies.util;

import br.com.adalbertofjr.popularmovies.BuildConfig;

/**
 * Popular Movies
 * Constants
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Constants {
    //EXTRA
    public static final String MOVIE_DETAIL_EXTRA = "movie";
    public static final String EXTRA_REVIEWS = "reviews";

    //Key for api The Movie Db
    private static final String THE_MOVIE_DB_API_KEY = BuildConfig.THE_MOVIE_DB_KEY;

    //The Movie DB paths
    public static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String MOVIES_POPULAR_PATH = "popular";
    public static final String MOVIES_TOP_RATED_PATH = "top_rated";
    private static final String MOVIES_API_KEY_PARAMETER = "?api_key=" + THE_MOVIE_DB_API_KEY;
    public static final String MOVIES_POPULAR_URL = MOVIES_BASE_URL + MOVIES_POPULAR_PATH + MOVIES_API_KEY_PARAMETER;
    public static final String MOVIES_TOP_RATED_URL = MOVIES_BASE_URL + MOVIES_TOP_RATED_PATH + MOVIES_API_KEY_PARAMETER;

    //Image The Movie DB paths
    private static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_IMAGE_W500_PATH = "w500/";
    private static final String MOVIE_IMAGE_W185_PATH = "w185/";
    private static final String MOVIE_VIDEO_PATH = "%s/videos";
    private static final String MOVIE_REVIEWS_PATH = "%s/reviews";

    public static final String MOVIE_IMAGE_BACKDROP_URL = Constants.MOVIE_IMAGE_BASE_URL + Constants.MOVIE_IMAGE_W500_PATH;
    public static final String MOVIE_IMAGE_POSTER_URL = Constants.MOVIE_IMAGE_BASE_URL + Constants.MOVIE_IMAGE_W185_PATH;
    public static final String MOVIE_TRAILERS_URL = Constants.MOVIES_BASE_URL + MOVIE_VIDEO_PATH + MOVIES_API_KEY_PARAMETER;
    public static final String MOVIE_REVIEWS_URL = Constants.MOVIES_BASE_URL + MOVIE_REVIEWS_PATH + MOVIES_API_KEY_PARAMETER;

    //Youtube paths
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    //Json movies objects names
    public static final String MOVIES_ID = "id";
    public static final String MOVIES_LIST_KEY = "results";
    public static final String MOVIES_BACKGROUND_KEY = "backdrop_path";
    public static final String MOVIES_POSTER_KEY = "poster_path";
    public static final String MOVIES_VOTE_AVERAGE_KEY = "vote_average";
    public static final String MOVIES_TITLE_KEY = "original_title";
    public static final String MOVIES_RELEASE_DATE_KEY = "release_date";

    public static final String MOVIES_OVERVIEW_KEY = "overview";

    //Json trailers objects names
    public static final String TRAILERS_VIDEOS_LIST_KEY = "results";
    public static final String TRAILERS_VIDEO_ID = "id";
    public static final String TRAILERS_VIDEO_KEY = "key";
    public static final String TRAILERS_VIDEO_NAME = "name";
    public static final String TRAILERS_VIDEO_SITE = "site";

    //Json reviews objects names
    public static final String REVIEWS_VIDEOS_LIST_KEY = "results";
    public static final String REVIEWS_VIDEO_ID = "id";
    public static final String REVIEWS_VIDEOS_AUTHOR = "author";
    public static final String REVIEWS_VIDEOS_CONTENT = "content";


}
