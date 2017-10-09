package com.entrevista.ifood2;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
