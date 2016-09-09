package br.com.adalbertofjr.popularmovies.model;

import br.com.adalbertofjr.popularmovies.util.Constants;

/**
 * Popular Movies
 * Trailers
 * <p/>
 * Created by Adalberto Fernandes Júnior on 09/09/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */

public class Trailers {
    private String key;
    private String name;
    private String site;

    public Trailers(String key, String name, String site) {
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTrailerUrlPath() {
        return Constants.YOUTUBE_BASE_URL + this.key;
    }
}
