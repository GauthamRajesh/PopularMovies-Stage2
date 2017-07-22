package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private List<Movie> mMovies;
    private Context mContext;
    private  String mfavString;
    // String... favString is supposed to be used if you want the activity to have the text saying the movie is favorited
    public MovieAdapter(Context context, List<Movie> movies, String... favString) {
        super(context, 0, movies);
        mMovies = movies;
        mContext = context;
        if(favString.length > 0) {
            mfavString = favString[0];
        }
        else {
            mfavString = getContext().getString(R.string.not_favorited);
        }
    }
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Movie movie = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_thumbnail, parent, false);
        }
        ImageView movieView = (ImageView) convertView.findViewById(R.id.movie_poster);
        movieView.setAdjustViewBounds(true);
        String baseURL = "http://image.tmdb.org/t/p/w185";
        Picasso.with(mContext).load(baseURL + mMovies.get(position).getPosterPath()).into(movieView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MovieDetail.class);
                Bundle strings = new Bundle();
                strings.putString("title", movie.getOriginalTitle());
                strings.putString("poster_path", movie.getPosterPath());
                strings.putString("synopsis", movie.getOverview());
                strings.putString("vote_avg", movie.getVoteAverage());
                strings.putString("release_date", movie.getReleaseDate());
                strings.putString("id", movie.getID());
                strings.putString("Fav_string", mfavString);
                i.putExtras(strings);
                mContext.startActivity(i);
            }
        });
        return convertView;
    }
}
