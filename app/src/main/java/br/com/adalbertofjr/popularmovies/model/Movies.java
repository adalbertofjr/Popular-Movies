package br.com.adalbertofjr.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.adalbertofjr.popularmovies.util.Constants;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * Popular Movies
 * Movies
 * <p/>
 * Created by Adalberto Fernandes Júnior on 16/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Movies implements Parcelable {
    private String backdrop_path;
    private String poster_path;
    private String vote_average;
    private String original_title;
    private String release_date;
    private String overview;

    public Movies() {
    }

    public Movies(String backdrop_path, String poster_path, String vote_average, String original_title, String release_date, String overview) {
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
    }

    protected Movies(Parcel in) {
        backdrop_path = in.readString();
        poster_path = in.readString();
        vote_average = in.readString();
        original_title = in.readString();
        release_date = in.readString();
        overview = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrlPath() {
        return Util.buildImageUrl(Constants.MOVIE_POSTER_PATH_URL + this.getPoster_path());
    }

    public String getBackDropUrlPath() {
        return Util.buildImageUrl(Constants.MOVIE_BACKDROP_PATH_URL + this.getBackdrop_path());
    }


    @Override
    public String toString() {
        return this.original_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(backdrop_path);
        parcel.writeString(poster_path);
        parcel.writeString(vote_average);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
    }
}
