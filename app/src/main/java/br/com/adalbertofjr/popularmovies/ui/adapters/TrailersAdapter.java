package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Trailer;

/**
 * Popular Movies
 * TrailersAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 09/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    private static final String LOG_TAG = TrailersAdapter.class.getSimpleName();
    private Context mContext;
    private List<Trailer> mTrailer;

    public TrailersAdapter(Context mContext, List<Trailer> mTrailer) {
        this.mContext = mContext;
        this.mTrailer = mTrailer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer trailer = mTrailer.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTrailer(trailer);
            }
        });
    }

    private void playTrailer(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.youtube");
        intent.setData(Uri.parse(trailer.getTrailerUrlPath()));

        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + trailer.getTrailerUrlPath() + ", no receiving apps installed!");
        }
    }

    @Override
    public int getItemCount() {
        return mTrailer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
