package br.com.adalbertofjr.popularmovies.ui.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Reviews;
import br.com.adalbertofjr.popularmovies.ui.adapters.ReviewsAdapter;
import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * Popular Movies
 * ReviewsDialogFragment
 * <p>
 * Created by Adalberto Fernandes Júnior on 10/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class ReviewsDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "reviews_dialog_tag";
    private List<Reviews> mReviews;

    public static ReviewsDialogFragment novaInstancia(List<Reviews> reviews) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.EXTRA_REVIEWS, (ArrayList<Reviews>) reviews);

        ReviewsDialogFragment dialog = new ReviewsDialogFragment();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviews = getArguments().getParcelableArrayList(Constants.EXTRA_REVIEWS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_reviews, null);

        RecyclerView recyclerViewReviews = (RecyclerView) v.findViewById(R.id.rv_dialog_reviews);
        recyclerViewReviews.setHasFixedSize(true);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), mReviews);
        recyclerViewReviews.setAdapter(reviewsAdapter);

        builder.setView(v);

        return builder.create();
    }

    public void abrir(android.app.FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }
}
