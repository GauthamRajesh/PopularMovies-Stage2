package com.example.android.popularmoviesstage2;



class Movie {
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
}
