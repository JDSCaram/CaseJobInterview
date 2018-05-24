package com.entrevista.ifood2.dagger.module.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {Created by Jonatas Caram on 17/05/2018}.
 */
@Module
public class SharedPrefsModule {


    private SharedPreferences preferences;

    public SharedPrefsModule(Application application) {
        preferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return preferences;
    }
}
