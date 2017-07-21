package com.example.android.popularmoviesstage2.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class MovieProvider extends ContentProvider {
    private static final int ALL_MOVIES = 50;
    private static final int SPECIFIC_MOVIE = 51;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES,  ALL_MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", SPECIFIC_MOVIE);
    }
    private static MovieDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c;
        int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_MOVIES:
                c = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SPECIFIC_MOVIE:
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                c = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_MOVIES:
                return MovieContract.MovieEntry.CONTENT_LIST_TYPE;
            case SPECIFIC_MOVIE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL_MOVIES:
                return updateMovie(uri, contentValues, selection, selectionArgs);
            case SPECIFIC_MOVIE:
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMovie(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateMovie(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numberRowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
        if (numberRowsUpdated == 0) {
            Log.e("PizzaProvider", "No rows updated for " + uri);
        } else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberRowsUpdated;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_MOVIES:
                return insertMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    public Uri insertMovie(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Log.e("PizzaProvider", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_MOVIES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SPECIFIC_MOVIE:
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
}
