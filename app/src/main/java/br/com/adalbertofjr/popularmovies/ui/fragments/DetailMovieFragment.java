package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import br.com.adalbertofjr.popularmovies.model.Movie;
import br.com.adalbertofjr.popularmovies.model.Review;
import br.com.adalbertofjr.popularmovies.model.Trailer;
import br.com.adalbertofjr.popularmovies.ui.adapters.TrailersAdapter;

/**
 * Popular Movies
 * DetailFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
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
    private AppBarLayout mAppBar;

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
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.novo_interesse_toolbar);
        mPosterImageBack = (ImageView) rootView.findViewById(R.id.iv_detail_poster_back);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        mPosterImage = (ImageView) rootView.findViewById(R.id.iv_detail_poster);
        ((ImageView) rootView.findViewById(R.id.iv_detail_star)).setImageResource(R.drawable.ic_star);
        mTitle = (TextView) rootView.findViewById(R.id.tv_detail_title);
        mDateRelease = (TextView) rootView.findViewById(R.id.tv_detail_dt_release);
        mVoteAverage = (TextView) rootView.findViewById(R.id.tv_detail_vote_average);
        mOverview = (TextView) rootView.findViewById(R.id.tv_detail_overview);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        mAuthorReviewOne = (TextView) rootView.findViewById(R.id.tv_detail_reviews_author_one);
        mContextReviewOne = (TextView) rootView.findViewById(R.id.tv_detail_reviews_content_one);
        mAuthorReviewTwo = (TextView) rootView.findViewById(R.id.tv_detail_reviews_author_two);
        mContextReviewTwo = (TextView) rootView.findViewById(R.id.tv_detail_reviews_content_two);
        mReadMoreView = (TextView) rootView.findViewById(R.id.tv_detail_reviews_more);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.coolapseToolbar);
        mContainerReview = rootView.findViewById(R.id.ll_detail_reviews);
        mTrailersListRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_detail_trailers);

        mActivity.setSupportActionBar(mToolbar);
        mAppBar = (AppBarLayout) rootView.findViewById(R.id.appBar);

        if (mAppBar != null) {
//            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
//                CoordinatorLayout.LayoutParams lp =
//                        (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
//                lp.height = getResources().getDisplayMetrics().widthPixels;
//            }
        }

        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTrailersListRecyclerView.setHasFixedSize(true);
        mTrailersListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

        Movie movie = new Movie();
        movie.setId(cursor.getString(0));
        movie.setOriginal_title(cursor.getString(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setRelease_date(cursor.getString(3));
        movie.setVote_average(cursor.getString(4));
        movie.setOverview(cursor.getString(5));
        movie.setBackdrop_path(cursor.getString(6));

        if (mCollapsingToolbarLayout != null) {
            mActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(movie.getOriginal_title());
        }

        Picasso.with(getContext())
                .load(movie.getBackDropUrlPath())
                .into(mPosterImageBack);

        Picasso.with(getContext())
                .load(movie.getPosterUrlPath())
                .into(mPosterImage);

        String dtRelease = formatDate(movie.getRelease_date());
        mTitle.setText(movie.getOriginal_title());
        mDateRelease.setText(dtRelease);
        mVoteAverage.setText(movie.getVote_average());
        mOverview.setText(movie.getOverview());

        List<Trailer> trailers = getTrailers(movie);
        updateTrailersAdapter(trailers);

        List<Review> reviews = getReviews(movie);
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
}
