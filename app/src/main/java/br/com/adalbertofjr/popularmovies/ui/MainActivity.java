package br.com.adalbertofjr.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesGridAdapter;
import br.com.adalbertofjr.popularmovies.ui.fragments.DetailMovieFragment;

public class MainActivity extends AppCompatActivity
        implements MoviesGridAdapter.OnMovieSelectedListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);

        if (mTwoPane) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_detail_container, new DetailMovieFragment(), DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    public void onMovieSelected(Movies movie, Uri uri) {
        String uriMovie = uri.buildUpon().appendPath(movie.getId()).toString();

        if (mTwoPane) {
            DetailMovieFragment fragment = DetailMovieFragment.newInstance(uriMovie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_detail_container, fragment, DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, uriMovie);
            startActivity(intent);
        }
    }
}
