package br.com.adalbertofjr.popularmovies.model;

/**
 * Popular Movies
 * Movies
 * <p>
 * Created by Adalberto Fernandes Júnior on 16/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Movies {
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

    @Override
    public String toString() {
        return this.original_title;
    }
}
