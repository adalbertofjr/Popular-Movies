package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adalbertofjr.popularmovies.model.Movies;

/**
 * Popular Movies
 * MoviesImageAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class MoviesImageAdapter extends ArrayAdapter<Movies> {
    public MoviesImageAdapter(Context context, List<Movies> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movies movie = getItem(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(200, 360));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }

        String MOVIE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
        Uri.Builder uriImage = Uri.parse(MOVIE_IMAGE_URL)
                .buildUpon()
                .appendEncodedPath(movie.getPoster_path());
        String urlPoster = uriImage.build().toString();
        Picasso.with(getContext()).load(urlPoster).into(imageView);
        return imageView;
    }
}
