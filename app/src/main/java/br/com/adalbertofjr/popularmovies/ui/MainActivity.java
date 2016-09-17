package br.com.adalbertofjr.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.ui.fragments.DetailMovieFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean mTwoPane = getResources().getBoolean(R.bool.has_two_panes);

        MoviesFragment moviesFragment = new MoviesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_movies_fragment,
                        moviesFragment, MoviesFragment.MOVIE_FRAGMENT_TAG)
                .commit();

        if (mTwoPane) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_detail_container, new DetailMovieFragment(), DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        }
    }
}
