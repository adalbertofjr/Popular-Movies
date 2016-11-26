package br.com.adalbertofjr.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import br.com.adalbertofjr.popularmovies.data.MoviesContract.FavoritesEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.PopularEntry;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.ReviewsEntry;
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

    static final int POPULAR = 100;
    static final int POPULAR_WITH_MOVIE = 101;
    static final int TOP_RATED = 200;
    static final int TOP_RATED_WITH_MOVIE = 201;
    static final int TRAILERS = 300;
    static final int TRAILERS_WITH_MOVIE = 301;
    static final int REVIEWS = 400;
    static final int REVIEWS_WITH_MOVIE = 401;
    static final int FAVORITES = 500;
    static final int FAVORITES_WITH_MOVIE = 501;
    private SQLiteOpenHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcherMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = MoviesContract.CONTENT_AUTHORITY;
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_POPULAR, POPULAR);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_POPULAR + "/#", POPULAR_WITH_MOVIE);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED, TOP_RATED);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED + "/#", TOP_RATED_WITH_MOVIE);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_FAVORITES, FAVORITES);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_FAVORITES + "/#", FAVORITES_WITH_MOVIE);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILERS);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/#", TRAILERS_WITH_MOVIE);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEWS);
        uriMatcherMatcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/#", REVIEWS_WITH_MOVIE);

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
            case POPULAR_WITH_MOVIE: {
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
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
            case TOP_RATED_WITH_MOVIE: {
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
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
            case FAVORITES: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITES_WITH_MOVIE: {
                selection = "_id = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                retCursor = mOpenHelper.getWritableDatabase().query(
                        FavoritesEntry.TABLE_NAME,
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
            case TRAILERS_WITH_MOVIE: {
                selection = "id_movie = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
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
            case REVIEWS: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEWS_WITH_MOVIE: {
                selection = "id_movie = ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                retCursor = mOpenHelper.getWritableDatabase().query(
                        ReviewsEntry.TABLE_NAME,
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
            case POPULAR_WITH_MOVIE: {
                return PopularEntry.CONTENT_ITEM_TYPE;
            }
            case TOP_RATED: {
                return TopRatedEntry.CONTENT_TYPE;
            }
            case TOP_RATED_WITH_MOVIE: {
                return TopRatedEntry.CONTENT_ITEM_TYPE;
            }
            case FAVORITES: {
                return FavoritesEntry.CONTENT_TYPE;
            }
            case FAVORITES_WITH_MOVIE: {
                return FavoritesEntry.CONTENT_ITEM_TYPE;
            }
            case TRAILERS: {
                return TrailersEntry.CONTENT_TYPE;
            }
            case TRAILERS_WITH_MOVIE: {
                return TrailersEntry.CONTENT_ITEM_TYPE;
            }
            case REVIEWS: {
                return ReviewsEntry.CONTENT_TYPE;
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
            case FAVORITES: {
                long _id = db.insert(FavoritesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoritesEntry.buildFavoritesMovieUri(values.getAsLong(TopRatedEntry._ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(TrailersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TrailersEntry.buildTrailersMovieUri(values.getAsLong(TrailersEntry.COLUMN_MOVIE_ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long _id = db.insert(ReviewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ReviewsEntry.buildReviewsMovieUri(values.getAsLong(ReviewsEntry.COLUMN_MOVIE_ID));
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
            case FAVORITES: {
                rowsDeleted = db.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILERS: {
                rowsDeleted = db.delete(TrailersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEWS: {
                rowsDeleted = db.delete(ReviewsEntry.TABLE_NAME, selection, selectionArgs);
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
            case FAVORITES: {
                rowsUpdate = db.update(MoviesContract.FavoritesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TRAILERS: {
                rowsUpdate = db.update(MoviesContract.TrailersEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case REVIEWS: {
                rowsUpdate = db.update(MoviesContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
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

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case POPULAR:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TOP_RATED:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
