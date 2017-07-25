package com.example.android.popularmoviesstage2;


import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable {
    private String mID;
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;
    Movie(String vID, String vOriginalTitle, String vPosterPath,
                 String vOverview, String vVoteAverage, String vReleaseDate) {
        mID = vID;
        mOriginalTitle = vOriginalTitle;
        mPosterPath = vPosterPath;
        mOverview = vOverview;
        mVoteAverage = vVoteAverage;
        mReleaseDate = vReleaseDate;
    }
    private Movie(Parcel in) {
        mID = in.readString();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = in.readString();
    }
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String getID() { return mID; }
    String getOriginalTitle(){
        return mOriginalTitle;
    }
    String getPosterPath(){
        return mPosterPath;
    }
    String getOverview(){
        return mOverview;
    }
    String getVoteAverage(){
        return mVoteAverage;
    }
    String getReleaseDate(){
        return mReleaseDate;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mVoteAverage);
        dest.writeString(mReleaseDate);
    }
}
