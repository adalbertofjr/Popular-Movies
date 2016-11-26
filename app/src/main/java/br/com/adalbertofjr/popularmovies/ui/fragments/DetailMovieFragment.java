package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.data.MoviesContract;
import br.com.adalbertofjr.popularmovies.data.MoviesContract.FavoritesEntry;
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.model.Review;
import br.com.adalbertofjr.popularmovies.model.Trailer;
import br.com.adalbertofjr.popularmovies.ui.DetailActivity;
import br.com.adalbertofjr.popularmovies.ui.adapters.TrailersAdapter;

/**
 * Popular Movies
 * DetailFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    public static final String DETAIL_MOVIE_FRAGMENT_TAG = "DMFTAG";
    private static final int DETAIL_LOADER = 0;
    private ProgressBar mProgressBar;
    private RecyclerView mTrailersListRecyclerView;
    private TextView mContextReviewOne;
    private TextView mAuthorReviewOne;
    private TextView mAuthorReviewTwo;
    private TextView mContextReviewTwo;
    private TextView mReadMoreView;
    private View mContainerReview;
    private String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    private String mMovieUri;
    private ImageView mPosterImage;
    private TextView mTitle;
    private TextView mDateRelease;
    private TextView mVoteAverage;
    private TextView mOverview;
    private AppCompatActivity mActivity;
    private ImageView mPosterImageBack;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFavorito;
    private Movie mMovie;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(String uri) {
        DetailMovieFragment dmf = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TEXT, uri);
        dmf.setArguments(bundle);

        return dmf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            mMovieUri = arguments.getString(Intent.EXTRA_TEXT);
        } else {
            // Todo - Corrigir filme inicial
            mMovieUri = MoviesContract.PopularEntry.buildPopularMovieUri(131631).toString();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = ((AppCompatActivity) getActivity());

        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        mFavorito = (FloatingActionButton) rootView.findViewById(R.id.fb_detail_favorito);
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.tb_detail);
        mPosterImageBack = (ImageView) rootView.findViewById(R.id.iv_detail_poster_back);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        mPosterImage = (ImageView) rootView.findViewById(R.id.iv_detail_poster);
        mTitle = (TextView) rootView.findViewById(R.id.tv_detail_title);
        mDateRelease = (TextView) rootView.findViewById(R.id.tv_detail_dt_release);
        mVoteAverage = (TextView) rootView.findViewById(R.id.tv_detail_vote_average);
        mOverview = (TextView) rootView.findViewById(R.id.tv_detail_overview);
        ((ImageView) rootView.findViewById(R.id.iv_detail_star)).setImageResource(R.drawable.ic_star);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        mAuthorReviewOne = (TextView) rootView.findViewById(R.id.tv_detail_reviews_author_one);
        mContextReviewOne = (TextView) rootView.findViewById(R.id.tv_detail_reviews_content_one);
        mAuthorReviewTwo = (TextView) rootView.findViewById(R.id.tv_detail_reviews_author_two);
        mContextReviewTwo = (TextView) rootView.findViewById(R.id.tv_detail_reviews_content_two);
        mReadMoreView = (TextView) rootView.findViewById(R.id.tv_detail_reviews_more);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.ct_detail);
        mContainerReview = rootView.findViewById(R.id.ll_detail_reviews);
        mTrailersListRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_detail_trailers);

        mActivity.setSupportActionBar(mToolbar);

        boolean mTwoPane = getActivity().getResources().getBoolean(R.bool.has_two_panes);
        boolean isDetailActivity = getActivity() instanceof DetailActivity;

        if (mTwoPane && !isDetailActivity) {
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            mCollapsingToolbarLayout.setVisibility(View.GONE);
        } else {
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        mTrailersListRecyclerView.setHasFixedSize(true);
        mTrailersListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mFavorito.setOnClickListener(this);

        return rootView;
    }

    private String formatDate(String date) {
        SimpleDateFormat formatFromApi = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatToApp = new SimpleDateFormat("MMMM yyyy");
        String formatDate;
        try {
            formatDate = formatToApp.format(formatFromApi.parse(date));
            return formatDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    private void hideProgressBar() {
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                Uri.parse(mMovieUri),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if (!cursor.moveToNext()) return;

        hideProgressBar();

        mMovie = new Movie();
        mMovie.setId(cursor.getString(0));
        mMovie.setOriginal_title(cursor.getString(1));
        mMovie.setPoster_path(cursor.getString(2));
        mMovie.setRelease_date(cursor.getString(3));
        mMovie.setVote_average(cursor.getString(4));
        mMovie.setOverview(cursor.getString(5));
        mMovie.setBackdrop_path(cursor.getString(6));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mCollapsingToolbarLayout.setTitle(mMovie.getOriginal_title());
        } else {
            mCollapsingToolbarLayout.setTitleEnabled(false);
            mActivity.getSupportActionBar().setTitle(mMovie.getOriginal_title());
        }

        if (isFavorito(mMovie.getId())) {
            mFavorito.setBackgroundTintList(getFabBackground(true));
        }

        Picasso.with(getContext())
                .load(mMovie.getBackDropUrlPath())
                .into(mPosterImageBack);

        Picasso.with(getContext())
                .load(mMovie.getPosterUrlPath())
                .into(mPosterImage);

        String dtRelease = formatDate(mMovie.getRelease_date());
        mTitle.setText(mMovie.getOriginal_title());
        mDateRelease.setText(dtRelease);
        mVoteAverage.setText(mMovie.getVote_average());
        mOverview.setText(mMovie.getOverview());

        List<Trailer> trailers = getTrailers(mMovie);
        updateTrailersAdapter(trailers);

        List<Review> reviews = getReviews(mMovie);
        updateReviewsAdapter(reviews);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateReviewsAdapter(final List<Review> reviews) {
        if (reviews == null || reviews.size() == 0) {
            return;
        }

        if (reviews.size() > 0) {
            mAuthorReviewOne.setVisibility(View.VISIBLE);
            mContextReviewOne.setVisibility(View.VISIBLE);
            mAuthorReviewOne.setText(reviews.get(0).getAuthor());
            mContextReviewOne.setText(reviews.get(0).getContent());
        }

        if (reviews.size() > 1) {
            mAuthorReviewTwo.setVisibility(View.VISIBLE);
            mContextReviewTwo.setVisibility(View.VISIBLE);
            mAuthorReviewTwo.setText(reviews.get(1).getAuthor());
            mContextReviewTwo.setText(reviews.get(1).getContent());
        }

        mContainerReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsDialogFragment reviewsDialog = ReviewsDialogFragment.novaInstancia(reviews);
                reviewsDialog.abrir(getActivity().getFragmentManager());
            }
        });

        mReadMoreView.setVisibility(View.VISIBLE);
    }

    private List<Review> getReviews(Movie movie) {
        Cursor cursorReviews = getActivity().getContentResolver()
                .query(
                        MoviesContract.ReviewsEntry.buildReviewsMovieUri(Long.valueOf(movie.getId())),
                        null,
                        null,
                        null,
                        null
                );

        List<Review> reviews = new ArrayList<>();

        while (cursorReviews.moveToNext()) {
            String id = cursorReviews.getString(cursorReviews.getColumnIndex("_id"));
            String idMovie = cursorReviews.getString(cursorReviews.getColumnIndex("id_movie"));
            String author = cursorReviews.getString(cursorReviews.getColumnIndex("author"));
            String content = cursorReviews.getString(cursorReviews.getColumnIndex("content"));

            Review review = new Review(id, idMovie, author, content);
            reviews.add(review);
        }

        if (cursorReviews != null) {
            cursorReviews.close();
        }
        return reviews;
    }

    private List<Trailer> getTrailers(Movie movie) {
        Cursor cursorTrailers = getActivity().getContentResolver()
                .query(
                        MoviesContract.TrailersEntry.buildTrailersMovieUri(Long.valueOf(movie.getId())),
                        null,
                        null,
                        null,
                        null
                );

        List<Trailer> trailers = new ArrayList<>();

        while (cursorTrailers.moveToNext()) {
            String id = cursorTrailers.getString(cursorTrailers.getColumnIndex("_id"));
            String idMovie = cursorTrailers.getString(cursorTrailers.getColumnIndex("id_movie"));
            String key = cursorTrailers.getString(cursorTrailers.getColumnIndex("key"));
            String name = cursorTrailers.getString(cursorTrailers.getColumnIndex("name"));
            String site = cursorTrailers.getString(cursorTrailers.getColumnIndex("site"));

            Trailer trailer = new Trailer(id, idMovie, key, name, site);
            trailers.add(trailer);
        }

        if (cursorTrailers != null) {
            cursorTrailers.close();
        }
        return trailers;
    }

    private void updateTrailersAdapter(List<Trailer> trailers) {
        TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), trailers);
        mTrailersListRecyclerView.setAdapter(trailersAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fb_detail_favorito) {
            if (mMovie != null) {
                setFavorito();
            }
        }
    }

    private void setFavorito() {
        if (isFavorito(mMovie.getId())) {
            String selection = FavoritesEntry._ID + "= ?";
            String[] selectionArgs = new String[]{mMovie.getId()};
            int delete = getActivity().getContentResolver().delete(FavoritesEntry.CONTENT_URI,
                    selection,
                    selectionArgs
            );

            if (delete != -1) {
                Log.i(LOG_TAG, "Favorito removido");
                mFavorito.setBackgroundTintList(getFabBackground(false));
            }
        } else {
            Uri insert = getActivity().getContentResolver().insert(FavoritesEntry.CONTENT_URI,
                    getMovieContentValues(mMovie));

            Long idMovie = ContentUris.parseId(insert);

            if (idMovie != -1) {
                Log.i(LOG_TAG, "Favorito inserido");
                mFavorito.setBackgroundTintList(getFabBackground(true));
            }
        }
    }

    private boolean isFavorito(String id) {
        String[] projection = {FavoritesEntry._ID};
        String selection = FavoritesEntry._ID + "= ?";
        String[] selectionArgs = new String[]{id};

        Cursor cursor = getActivity().getContentResolver().query(
                FavoritesEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor.moveToNext()) {
            return true;
        }

        return false;
    }

    private ColorStateList getFabBackground(boolean favorito) {
        return getResources().getColorStateList(favorito
                ? R.color.bg_fab_favorito : R.color.bg_fab_cancel);
    }

    private ContentValues getMovieContentValues(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(FavoritesEntry._ID, movie.getId());
        movieValues.put(FavoritesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        movieValues.put(FavoritesEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        movieValues.put(FavoritesEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(FavoritesEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        movieValues.put(FavoritesEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(FavoritesEntry.COLUMN_BACKDROP_PATH, movie.getBackDropUrlPath());
        return movieValues;
    }
}
