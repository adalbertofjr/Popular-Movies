package br.com.adalbertofjr.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;

/**
 * Popular Movies
 * MoviesDbHelper
 * <p/>
 * Created by Adalberto Fernandes Júnior on 18/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " +
                PopularEntry.TABLE_NAME + " (" +
                PopularEntry._ID + " INTEGER PRIMATY KEY, " +
                PopularEntry.COLUMN_ORIGINAL_TITLE + " TEXT UNIQUE NOT NULL, " +
                PopularEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                PopularEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                PopularEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + PopularEntry.TABLE_NAME);
    }
}
