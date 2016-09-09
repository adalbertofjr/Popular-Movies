package br.com.adalbertofjr.popularmovies.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.adalbertofjr.popularmovies.R;
import br.com.adalbertofjr.popularmovies.model.Trailers;

/**
 * Popular Movies
 * TrailersAdapter
 * <p/>
 * Created by Adalberto Fernandes Júnior on 09/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    private Context mContext;
    private List<Trailers> mTrailer;

    public TrailersAdapter(Context mContext, List<Trailers> mTrailer) {
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
        Trailers trailer = mTrailer.get(position);
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
