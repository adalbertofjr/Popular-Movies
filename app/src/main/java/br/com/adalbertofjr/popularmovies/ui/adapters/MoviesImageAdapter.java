package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.util.Constants;

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

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.movies_item, parent, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_movies_item_poster);
        Uri.Builder uriImage = Uri.parse(Constants.MOVIE_IMAGE_POSTER_URL)
                .buildUpon()
                .appendEncodedPath(movie.getPoster_path());
        String urlPoster = uriImage.build().toString();
        Picasso.with(getContext()).load(urlPoster).into(imageView);

        return rootView;
    }
}
