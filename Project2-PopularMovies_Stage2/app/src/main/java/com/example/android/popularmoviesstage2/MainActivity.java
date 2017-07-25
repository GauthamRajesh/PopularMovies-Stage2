package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.NetworkUtils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MovieAdapter movieAdapter;
    GridView gv;
    ArrayList<Movie> allMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GridView) findViewById(R.id.gvMovies);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        String baseUrl = "http://api.themoviedb.org/3/movie/popular";
        allMovies = new ArrayList<>();
        if(savedInstanceState != null && savedInstanceState.getParcelableArrayList("movies") != null) {
            allMovies = savedInstanceState.getParcelableArrayList("movies");
            gv.setAdapter(new MovieAdapter(this, allMovies));
        }
        if(isConnected) {
            new MovieTask().execute(baseUrl);
        }
        else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("movies", allMovies);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.most_popular_setting: {
                String baseUrl = "http://api.themoviedb.org/3/movie/popular";
                new MovieTask().execute(baseUrl);
                break;
            }
            case R.id.best_rated_setting: {
                String baseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                new MovieTask().execute(baseUrl);
                break;
            }
            case R.id.favorites_screen: {
                startActivity(new Intent(this, FavoritesActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public class MovieTask extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected Movie[] doInBackground(String... params) {
            String baseUrl = params[0];
            String apiParam = "api_key";
            Uri moviesUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(apiParam, getString(R.string.api_key))
                    .build();
            String jsonMovieString = null;
            try {
                jsonMovieString = NetworkUtils.getResponseFromHttpUrl(moviesUri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return getMovieData(jsonMovieString);
            } catch (JSONException e) {
                Log.e(getString(R.string.LOG_TAG), e.getMessage(), e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                for (int i = 0; i < movies.length; i++) {
                    allMovies.add(movies[i]);
                }
                movieAdapter = new MovieAdapter(MainActivity.this, allMovies);
                gv = (GridView) findViewById(R.id.gvMovies);
                gv.setAdapter(movieAdapter);
            }
        }
    }
    private Movie[] getMovieData(String jsonString) throws JSONException {
        final String results_json = "results";
        final String id_json = "id";
        final String title_json = "original_title";
        final String poster_json = "poster_path";
        final String synopsis_json = "overview";
        final String rating_json = "vote_average";
        final String date_json = "release_date";
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            JSONObject moviesJsonObject = new JSONObject(jsonString);
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(results_json);
            Movie[] movies = new Movie[moviesJsonArray.length()];
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject movie = moviesJsonArray.getJSONObject(i);
                movies[i] = new Movie(
                        movie.getString(id_json),
                        movie.getString(title_json),
                        movie.getString(poster_json),
                        movie.getString(synopsis_json),
                        movie.getString(rating_json),
                        movie.getString(date_json)
                );
            }
            return movies;
        } else {
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }
    }
}

