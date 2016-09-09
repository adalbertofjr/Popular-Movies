package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.squareup.picasso.Callback;
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
import br.com.adalbertofjr.popularmovies.ui.adapters.ReviewsAdapter;
import br.com.adalbertofjr.popularmovies.ui.adapters.TrailersAdapter;
import br.com.adalbertofjr.popularmovies.util.Constants;
import br.com.adalbertofjr.popularmovies.util.Util;

/**
 * Popular Movies
 * DetailFragment
 * <p/>
 * Created by Adalberto Fernandes Júnior on 10/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class DetailMovieFragment extends Fragment {
    private Movies mMovie;
    private ProgressBar mProgressBar;
    private RecyclerView mTrailersListRecyclerView;
    private RecyclerView mReviewsListRecyclerView;

    public DetailMovieFragment() {
    }

    public static DetailMovieFragment newInstance(Movies movie) {
        DetailMovieFragment dmf = new DetailMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MOVIE_DETAIL_EXTRA, movie);
        dmf.setArguments(bundle);

        return dmf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = getArguments().getParcelable(Constants.MOVIE_DETAIL_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_detail_progress);

        mTrailersListRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_detail_trailers);
        mReviewsListRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_detail_reviews);

        mTrailersListRecyclerView.setHasFixedSize(true);
        mTrailersListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mReviewsListRecyclerView.setHasFixedSize(true);
        mReviewsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        TextView errorMessage = (TextView) rootView.findViewById(R.id.tv_detail_error_message);

        if (mMovie != null) {
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (supportActionBar != null) {
                supportActionBar.setTitle(mMovie.getOriginal_title());
            }


            if (Util.isConnected(getActivity())) {
                Picasso.with(getContext())
                        .load(mMovie.getPosterUrlPath())
                        .into((ImageView) rootView.findViewById(R.id.iv_detail_poster), new Callback() {
                            @Override
                            public void onSuccess() {
                                hideProgressBar();
                                String dtRelease = formatDate(mMovie.getRelease_date());
                                ((TextView) rootView.findViewById(R.id.tv_detail_title)).setText(mMovie.getOriginal_title());
                                ((TextView) rootView.findViewById(R.id.tv_detail_dt_release)).setText(dtRelease);
                                ((ImageView) rootView.findViewById(R.id.iv_detail_star)).setImageResource(R.drawable.ic_star);
                                ((TextView) rootView.findViewById(R.id.tv_detail_vote_average)).setText(mMovie.getVote_average());
                                ((TextView) rootView.findViewById(R.id.tv_detail_overview)).setText(mMovie.getOverview());

                                new FetchTrailersTask().execute();
                                new FetchReviewsTask().execute();
                            }

                            @Override
                            public void onError() {

                            }
                        });
            } else {
                hideProgressBar();
                errorMessage.setVisibility(View.VISIBLE);
            }
        }

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

    private class FetchTrailersTask extends AsyncTask<Void, Void, ArrayList<Trailers>> {
        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        @Override
        protected ArrayList<Trailers> doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String trailersJsonString;

            try {

                if (mMovie == null)
                    return null;

                String pathTrailer = String.format(Constants.MOVIE_TRAILERS_URL, mMovie.getId());

                URL url = new URL(pathTrailer);

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

                trailersJsonString = buffer.toString();

                try {
                    return getMoviesDataFromJson(trailersJsonString);
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
        protected void onPostExecute(ArrayList<Trailers> trailers) {
            super.onPostExecute(trailers);

            for (Trailers t : trailers) {
                Log.i("Trailer", t.getTrailerUrlPath());
            }
            updateTrailersAdapter(trailers);
        }
    }

    private void updateTrailersAdapter(ArrayList<Trailers> trailers) {
        TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), trailers);
        mTrailersListRecyclerView.setAdapter(trailersAdapter);
    }

    private ArrayList<Trailers> getMoviesDataFromJson(String trailersJsonString)
            throws JSONException {

        JSONObject trailersJson = new JSONObject(trailersJsonString);
        JSONArray trailersArray = trailersJson.getJSONArray(Constants.TRAILERS_VIDEOS_LIST_KEY);

        ArrayList<Trailers> trailers = new ArrayList<>();

        for (int i = 0; i < trailersArray.length(); i++) {
            String key;
            String name;
            String site;

            JSONObject trailerData = trailersArray.getJSONObject(i);

            key = trailerData.getString(Constants.TRAILERS_VIDEO_KEY);
            name = trailerData.getString(Constants.TRAILERS_VIDEO_NAME);
            site = trailerData.getString(Constants.TRAILERS_VIDEO_SITE);

            Trailers trailer = new Trailers(
                    key,
                    name,
                    site
            );

            trailers.add(trailer);
        }

        return trailers;
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

    private void updateReviewsAdapter(List<Reviews> reviews) {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), reviews);
        mReviewsListRecyclerView.setAdapter(reviewsAdapter);
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
