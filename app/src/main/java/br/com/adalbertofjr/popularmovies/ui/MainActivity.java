package br.com.adalbertofjr.popularmovies.ui;

import android.content.Intent;
import android.database.Cursor;
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
import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.ui.adapters.MoviesGridAdapter;
import br.com.adalbertofjr.popularmovies.ui.fragments.DetailMovieFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.FavoritesMoviesFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.PopularMoviesFragment;
import br.com.adalbertofjr.popularmovies.ui.fragments.TopRatedMoviesFragment;

public class MainActivity extends AppCompatActivity
        implements MoviesGridAdapter.OnMovieSelectedListener {

    private static final String URI_SELECTED_STATE = "uri_state";
    private static final String TAB_SELECTED_STATE = "tab_state";
    private boolean mTwoPane;
    private int mTabSelected = 0;
    private String mUriMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mUriMovie = savedInstanceState.getString(URI_SELECTED_STATE, null);
            mTabSelected = savedInstanceState.getInt(TAB_SELECTED_STATE, -1);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tl_tabs);

        mToolbar.setTitleTextColor(Color.WHITE);
        mViewPager.setAdapter(new PopularMoviesPageAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabSelected = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (mTwoPane) {
            String uriMovie;
            if (mUriMovie != null) {
                uriMovie = mUriMovie;
            } else {
                Movie movie;
                switch (mTabSelected) {
                    case 0:
                        movie = getMovieStart(MoviesContract.PopularEntry.CONTENT_URI);
                        uriMovie = MoviesContract.PopularEntry.buildMoviesPopular(movie.getId()).toString();
                        break;
                    case 1:
                        movie = getMovieStart(MoviesContract.TopRatedEntry.CONTENT_URI);
                        uriMovie = MoviesContract.TopRatedEntry.buildMoviesTopRated(movie.getId()).toString();
                        break;
                    default:
                        movie = getMovieStart(MoviesContract.FavoritesEntry.CONTENT_URI);
                        if (movie != null) {
                            uriMovie = MoviesContract.FavoritesEntry.buildMoviesFavorites(movie.getId()).toString();
                        } else {
                            uriMovie = MoviesContract.FavoritesEntry.CONTENT_URI.toString();
                        }
                        break;
                }
            }

            DetailMovieFragment fragment = DetailMovieFragment.newInstance(uriMovie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_detail_container, fragment, DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                    .commit();
        }
    }

    private Movie getMovieStart(Uri contentUri) {
        Movie movie = new Movie();

        Cursor cursor = getContentResolver().query(contentUri,
                new String[]{"_id", "vote_average"},
                null,
                null,
                "vote_average desc");

        if (cursor.moveToNext()) {
            movie.setId(cursor.getString(0));
        } else {
            return null;
        }

        cursor.close();

        return movie;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(URI_SELECTED_STATE, mUriMovie);
        outState.putInt(TAB_SELECTED_STATE, mTabSelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieSelected(Movie movie, Uri uri) {
        mUriMovie = uri.buildUpon().appendPath(movie.getId()).toString();

        if (mTwoPane) {
            DetailMovieFragment fragment = DetailMovieFragment.newInstance(mUriMovie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_detail_container, fragment, DetailMovieFragment.DETAIL_MOVIE_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mUriMovie);
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
