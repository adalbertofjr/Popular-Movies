package br.com.adalbertofjr.popularmovies.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesGridAdapter;
import br.com.adalbertofjr.popularmovies.ui.fragments.DetailMovieFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.FavoritesMoviesFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.PopularMoviesFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.TopRatedMoviesFragment;

public class MainActivity extends AppCompatActivity
        implements MoviesGridAdapter.OnMovieSelectedListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tl_tabs);

//        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);
        mViewPager.setAdapter(new PopularMoviesPageAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);

        if (mTwoPane) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_detail_container, new DetailMovieFragment(), DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                        .commit();
            }
        }

//        PopularMoviesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onMovieSelected(Movie movie, Uri uri) {
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

    @Override
    public void onMoviePosition(int position) {

    }


    private class PopularMoviesPageAdapter extends FragmentPagerAdapter {
        public PopularMoviesPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PopularMoviesFragment();
            }

            return position == 1 ? new TopRatedMoviesFragment() :
                    new FavoritesMoviesFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.title_most_popular);
            }

            return position == 1 ?
                    getString(R.string.title_top_rated) :
                    getString(R.string.title_favorite);
        }
    }
}
