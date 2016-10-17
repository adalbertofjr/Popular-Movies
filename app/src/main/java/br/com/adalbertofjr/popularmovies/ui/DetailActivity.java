package br.com.adalbertofjr.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.ui.fragments.DetailMovieFragment;

/**
 * Popular Movies
 * DetailActivity
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String uri = intent.getStringExtra(Intent.EXTRA_TEXT);

        DetailMovieFragment dmf = DetailMovieFragment.newInstance(uri);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_detail_container, dmf, null)
                .commit();
    }
}
