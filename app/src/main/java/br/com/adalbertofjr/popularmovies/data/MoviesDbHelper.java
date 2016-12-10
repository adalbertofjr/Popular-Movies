package br.com.adalbertofjr.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.FavoritesEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.ReviewsEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.TopRatedEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.TrailersEntry;

/**
 * Popular Movies
 * MoviesDbHelper
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularmovies.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " +
                PopularEntry.TABLE_NAME + " (" +
                PopularEntry._ID + " INTEGER PRIMARY KEY, " +
                PopularEntry.COLUMN_ORIGINAL_TITLE + " TEXT UNIQUE NOT NULL, " +
                PopularEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                PopularEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " +
                TopRatedEntry.TABLE_NAME + " (" +
                TopRatedEntry._ID + " INTEGER PRIMARY KEY, " +
                TopRatedEntry.COLUMN_ORIGINAL_TITLE + " TEXT UNIQUE NOT NULL, " +
                TopRatedEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                TopRatedEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoritesEntry.COLUMN_ORIGINAL_TITLE + " TEXT UNIQUE NOT NULL, " +
                FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " +
                TrailersEntry.TABLE_NAME + " (" +
                TrailersEntry._ID + " TEXT PRIMARY KEY, " +
                TrailersEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailersEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailersEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TrailersEntry.COLUMN_SITE + " TEXT NOT NULL " +
                ");";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " +
                ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " TEXT PRIMARY KEY, " +
                ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + TopRatedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + TrailersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + ReviewsEntry.TABLE_NAME);
    }
}
