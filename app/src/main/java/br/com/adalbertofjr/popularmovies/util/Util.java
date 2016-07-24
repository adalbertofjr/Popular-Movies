package br.com.adalbertofjr.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import br.com.adalbertofjr.popularmovies.R;

/**
 * Popular Movies
 * Util
 * <p/>
 * Created by Adalberto Fernandes Júnior on 17/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Util {


    /**
     * Recupera o tipo de busca por filmes (Most Popular/Top Rated)
     *
     * @param context
     * @return url com o tipo de busca por filmes
     */
    public static String getOptionSortFetchMovies(Context context) {
        String option = getSortOptionPreference(context);
        if (option.equals(Constants.MOVIES_POPULAR_PATH)) {
            return Constants.MOVIES_POPULAR_URL;
        }

        return Constants.MOVIES_TOP_RATED_URL;
    }

    @NonNull
    public static String getSortOptionPreference(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(context.getString(R.string.pref_sort_options_key),
                context.getString(R.string.pref_sort_option_default));
    }

    /**
     * Monta o caminho da imagem
     *
     * @param path
     * @return string url
     */
    public static String buildUrl(String path) {
        Uri.Builder uriBackdrop = Uri.parse(path)
                .buildUpon();
        return uriBackdrop.build().toString();
    }

    //Thank's to http://stackoverflow.com/a/4009133
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
