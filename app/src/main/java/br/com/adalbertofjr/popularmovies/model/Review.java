package br.com.adalbertofjr.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Popular Movies
 * Reviews
 * <p/>
 * Created by Adalberto Fernandes Júnior on 09/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Review implements Parcelable {
    private String id;
    private String idMovie;
    private String author;
    private String content;

    public Review(String id, String idMovie, String author, String content) {
        this.id = id;
        this.idMovie = idMovie;
        this.author = author;
        this.content = content;
    }

    protected Review(Parcel in) {
        id = in.readString();
        idMovie = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idMovie);
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
