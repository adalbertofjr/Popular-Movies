package br.com.adalbertofjr.popularmovies.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * PopularMovies
 * PopularMovies
 * <p>
 * Created by Adalberto Fernandes Júnior on 12/10/2016.
 * Copyright © 2016 - Adalberto Fernandes Júnior. All rights reserved.
 */
public class PopularMovies extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
