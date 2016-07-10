package br.com.adalbertofjr.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.popularmovies.ui.fragments.SettingsFragment;

/**
 * Popular Movies
 * SettingsActivity
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content,
                        new SettingsFragment()).commit();
    }
}
