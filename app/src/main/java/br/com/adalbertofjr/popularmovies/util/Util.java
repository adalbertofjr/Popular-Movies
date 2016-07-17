package br.com.adalbertofjr.popularmovies.util;

import android.net.Uri;

/**
 * Popular Movies
 * Util
 * <p/>
 * Created by Adalberto Fernandes Júnior on 17/07/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Util {

    /**
     * Monta o caminho da imagem
     * @param path
     * @return
     */
    public static String buildImageUrl(String path) {
        Uri.Builder uriBackdrop = Uri.parse(path)
                .buildUpon();
        return uriBackdrop.build().toString();
    }
}
