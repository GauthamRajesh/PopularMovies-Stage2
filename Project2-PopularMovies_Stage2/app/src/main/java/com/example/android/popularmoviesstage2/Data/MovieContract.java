package com.example.android.popularmoviesstage2.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    private MovieContract() {
    }
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_MOVIE_NAME = "Name";
        public static final String COLUMN_MOVIE_OVERVIEW = "Overview";
        public static final String COLUMN_MOVIE_RATING = "Rating";
        public static final String COLUMN_MOVIE_DATE = "Date";
        public static final String COLUMN_MOVIE_IMAGE = "Image";
        public static final String COLUMN_MOVIE_ID = "Movie_id";
        public static final String COLUMN_FAVORITE_MOVIE = "Favorited";

    }
}
