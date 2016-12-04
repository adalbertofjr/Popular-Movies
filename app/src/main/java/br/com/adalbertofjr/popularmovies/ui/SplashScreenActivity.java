package br.com.adalbertofjr.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.sync.PopularMoviesSyncAdapter;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        PopularMoviesSyncAdapter.initializeSyncAdapter(this);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }
}
