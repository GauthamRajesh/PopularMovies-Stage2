package com.example.android.popularmoviesstage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.Data.MovieContract;
import com.example.android.popularmoviesstage2.NetworkUtils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MovieDetail extends AppCompatActivity {

    String fullVideoUrl = "";
    String reviewUrl = "";
    String title = "";
    String posterPath = "";
    String voteAvg = "";
    String synopsis = "";
    String releaseDate = "";
    String id = "";
    TextView favoritedTextView;
    String favsString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent i = getIntent();
        title = i.getStringExtra("title");
        posterPath = i.getStringExtra("poster_path");
        voteAvg = i.getStringExtra("vote_avg");
        synopsis = i.getStringExtra("synopsis");
        releaseDate = i.getStringExtra("release_date");
        id = i.getStringExtra("id");
        favsString = i.getStringExtra("Fav_string");
        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView voteTextView = (TextView) findViewById(R.id.vote_average);
        TextView synopsisTextView = (TextView) findViewById(R.id.synopsis);
        TextView dateTextView = (TextView) findViewById(R.id.date);
        favoritedTextView = (TextView) findViewById(R.id.isFavorited);
        ImageView posterView = (ImageView) findViewById(R.id.movie_poster);
        titleTextView.setText(title);
        voteTextView.setText(voteAvg);
        synopsisTextView.setText(synopsis);
        dateTextView.setText(releaseDate);
        favoritedTextView.setText(favsString);
        String baseURL = "http://image.tmdb.org/t/p/w185";
        Picasso.with(this).load(baseURL + posterPath).into(posterView);
        if(isConnected(this)) {
            new TrailerTask().execute(id);
            new ReviewTask().execute(id);
        }
    }
    public class TrailerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            Context c = MovieDetail.this;
            Uri.Builder builder = new Uri.Builder();
            Uri trailerUri = builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(id)
                    .appendPath("videos")
                    .appendQueryParameter("api_key", c.getString(R.string.api_key)).build();
            String jsonString = null;
            if(isConnected(c)) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(trailerUri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(c, c.getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
            try {
                return getTrailerId(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        public void onPostExecute(String s) {
            fullVideoUrl = getString(R.string.base_youtube_url).concat(s);
        }
    }
    public class ReviewTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Context c = MovieDetail.this;
            String id = params[0];
            Uri.Builder builder = new Uri.Builder();
            Uri reviewUri = builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(id)
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", MovieDetail.this.getString(R.string.api_key)).build();
            String jsonString = null;
            if(isConnected(c)) {
                try {
                    jsonString = NetworkUtils.getResponseFromHttpUrl(reviewUri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(c, c.getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
            try {
                return getReviewData(jsonString);
            } catch(JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        public void onPostExecute(String s) {
            if(s == null) {
                return;
            }
            reviewUrl = s;
        }
    }
    public static boolean isConnected(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public String getTrailerId(String s) throws JSONException {
        String results_json = "results";
        JSONObject moviesJsonObject = new JSONObject(s);
        JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(results_json);
        JSONObject trailer = moviesJsonArray.getJSONObject(0);
        return trailer.getString("key");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.extras_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.trailer: {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(fullVideoUrl));
                startActivity(i);
            }
            case R.id.review: {
                if(reviewUrl.equals("") || reviewUrl == null) {
                    Toast.makeText(this, getString(R.string.no_reviews), Toast.LENGTH_LONG).show();
                    return super.onOptionsItemSelected(item);
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviewUrl)));
            }
            case R.id.favorite: {
                if(isAlreadyFavorited(title)) {
                    break;
                }
                ContentValues cv = new ContentValues();
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, title);
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, synopsis);
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, Double.parseDouble(voteAvg));
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, posterPath);
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, releaseDate);
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
                favoritedTextView.setText(R.string.already_favorited);
                if(uri == null) {
                    Toast.makeText(this, R.string.favorites_failure, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, R.string.failure_success, Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public String getReviewData(String s) throws JSONException {
        String results_json = "results";
        JSONObject moviesJsonObject = new JSONObject(s);
        JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(results_json);
        if(moviesJsonArray.length() == 0) {
            return null;
        }
        JSONObject review = moviesJsonArray.getJSONObject(0);
        return review.getString("url");
    }
    public boolean isAlreadyFavorited(String title) {
        Cursor favoritesCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if(favoritesCursor != null && favoritesCursor.moveToNext()) {
            String favoritesTitle = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME));
            if(title.equals(favoritesTitle)) {
                return true;
            }
        }
        assert favoritesCursor != null;
        favoritesCursor.close();
        return false;
    }
}
