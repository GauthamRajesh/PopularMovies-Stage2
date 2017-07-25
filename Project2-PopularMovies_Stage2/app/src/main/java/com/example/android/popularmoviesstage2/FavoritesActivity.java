package com.example.android.popularmoviesstage2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.Data.MovieContract;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 30;
    private ArrayList<Movie> favoriteMovies;
    private GridView favoritesView;
    private String favorited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favoriteMovies = new ArrayList<>();
        favoritesView = (GridView) findViewById(R.id.favorites_gridview);
        favorited = "";
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID) {
            if(data == null) {
                Toast.makeText(this, R.string.no_favs, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            while(data.moveToNext()) {
                String title = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME));
                String id = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                String posterPath = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE));
                double rating = data.getDouble(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING));
                String overview = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                String date = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE));
                favorited = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE_MOVIE));
                favoriteMovies.add(new Movie(id, title, posterPath, overview, Double.toString(rating), date));
            }
        }
        data.close();
        MovieAdapter favsAdapter = new MovieAdapter(this, favoriteMovies, favorited);
        favoritesView.setAdapter(favsAdapter);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
