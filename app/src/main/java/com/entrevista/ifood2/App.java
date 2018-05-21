package com.entrevista.ifood2;

import android.app.Application;

import com.entrevista.ifood2.dagger.component.AppComponent;
import com.entrevista.ifood2.dagger.component.DaggerAppComponent;
import com.entrevista.ifood2.dagger.module.application.RetrofitModule;
import com.entrevista.ifood2.dagger.module.application.RoomModule;
import com.entrevista.ifood2.dagger.module.application.SharedPrefsModule;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        initDaggerApplicationComponent();
    }

    private void initDaggerApplicationComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .roomModule(new RoomModule(this))
                .retrofitModule(new RetrofitModule(BuildConfig.BASE_URL))
                .sharedPrefsModule(new SharedPrefsModule(this))
                .build();
        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
