package com.example.android.popularmoviesstage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " FLOAT NOT NULL DEFAULT 0, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_DATE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_FAVORITE_MOVIE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
