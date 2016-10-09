package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.TopRatedEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.TrailersEntry;

/**
 * PopularMovies
 * MoviesProvider
 * <p>
 * Created by Adalberto Fernandes Júnior on 01/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int POPULAR = 100;
    private static final int TOP_RATED = 200;
    private static final int TRAILERS = 300;
    private SQLiteOpenHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcherMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = MoviesContract.CONTENT_AUTHORITY;
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_POPULAR, POPULAR);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED, TOP_RATED);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILERS);

        return uriMatcherMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case POPULAR: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOP_RATED: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR: {
                return PopularEntry.CONTENT_TYPE;
            }
            case TOP_RATED: {
                return TopRatedEntry.CONTENT_TYPE;
            }
            case TRAILERS: {
                return TrailersEntry.CONTENT_TYPE;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case POPULAR: {
                long _id = db.insert(PopularEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = PopularEntry.buildPopularMovieUri(values.getAsLong(PopularEntry._ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOP_RATED: {
                long _id = db.insert(TopRatedEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TopRatedEntry.buildTopRatedMovieUri(values.getAsLong(TopRatedEntry._ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(TrailersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TrailersEntry.buildTrailersMovieUri(values.getAsLong(TrailersEntry._ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match) {
            case POPULAR: {
                rowsDeleted = db.delete(PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TOP_RATED: {
                rowsDeleted = db.delete(TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdate;
        switch (match) {
            case POPULAR: {
                rowsUpdate = db.update(MoviesContract.PopularEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TOP_RATED: {
                rowsUpdate = db.update(MoviesContract.TopRatedEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdate != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Student: return the actual rows updated
        return rowsUpdate;
    }
}
