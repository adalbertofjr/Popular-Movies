package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Movies;
import br.com.adalbertofjr.popularmovies.model.Reviews;
import br.com.adalbertofjr.popularmovies.model.Trailers;
import br.com.adalbertofjr.popularmovies.ui.adapters.TrailersAdapter;
import br.com.adalbertofjr.popularmovies.util.Constants;

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
    private Movies mMovie;
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
//            mMovie = arguments.getParcelable(Constants.MOVIE_DETAIL_EXTRA);
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
        final View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);
        ((ImageView) rootView.findViewById(R.id.iv_detail_star)).setImageResource(R.drawable.ic_star);
        mPosterImage = (ImageView) rootView.findViewById(R.id.iv_detail_poster);
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

        mContainerReview = rootView.findViewById(R.id.ll_detail_reviews);
        mTrailersListRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_detail_trailers);

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

        Movies movie = new Movies();
        movie.setId(cursor.getString(0));
        movie.setOriginal_title(cursor.getString(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setRelease_date(cursor.getString(3));
        movie.setVote_average(cursor.getString(4));
        movie.setOverview(cursor.getString(5));
        movie.setBackdrop_path(cursor.getString(6));

//        mMovie = movie;

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setTitle(movie.getOriginal_title());
        }

        Picasso.with(getContext())
                .load(movie.getPosterUrlPath())
                .into(mPosterImage);

        String dtRelease = formatDate(movie.getRelease_date());
        mTitle.setText(movie.getOriginal_title());
        mDateRelease.setText(dtRelease);
        mVoteAverage.setText(movie.getVote_average());
        mOverview.setText(movie.getOverview());

//        new FetchTrailersTask(getActivity()).execute(movie);
//        new FetchReviewsTask().execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateTrailersAdapter(ArrayList<Trailers> trailers) {
        TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), trailers);
        mTrailersListRecyclerView.setAdapter(trailersAdapter);
    }

    private class FetchReviewsTask extends AsyncTask<Void, Void, ArrayList<Reviews>> {
        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected ArrayList<Reviews> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String reviewsJsonString;

            try {

                if (mMovie == null)
                    return null;

                String pathReview = String.format(Constants.MOVIE_REVIEWS_URL, mMovie.getId());

                URL url = new URL(pathReview);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                reviewsJsonString = buffer.toString();

                try {
                    return getReviewsDataFromJson(reviewsJsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviews) {
            super.onPostExecute(reviews);

            for (Reviews t : reviews) {
                Log.i("Trailer", t.getAuthor());
            }
            updateReviewsAdapter(reviews);
        }
    }

    private void updateReviewsAdapter(final List<Reviews> reviews) {
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

    private ArrayList<Reviews> getReviewsDataFromJson(String reviewsJsonString)
            throws JSONException {

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);
        JSONArray reviewsArray = reviewsJson.getJSONArray(Constants.REVIEWS_VIDEOS_LIST_KEY);

        ArrayList<Reviews> reviews = new ArrayList<>();

        for (int i = 0; i < reviewsArray.length(); i++) {
            String name;
            String content;

            JSONObject reviewData = reviewsArray.getJSONObject(i);

            name = reviewData.getString(Constants.REVIEWS_VIDEOS_AUTHOR);
            content = reviewData.getString(Constants.REVIEWS_VIDEOS_CONTENT);

            Reviews review = new Reviews(
                    name,
                    content
            );

            reviews.add(review);
        }

        return reviews;
    }
}
