package com.entrevista.ifood2.repository.data;


import android.content.SharedPreferences;

import com.entrevista.ifood2.storage.database.AppDataBase;

import javax.inject.Inject;


/**
 * Created by JCARAM on 09/10/2017.
 */

public class LocalData {
    private AppDataBase mDatabase;
    private SharedPreferences prefs;

    @Inject
    public LocalData(AppDataBase mDatabase, SharedPreferences prefs) {
        this.mDatabase = mDatabase;
        this.prefs = prefs;
    }

    public AppDataBase getDatabase() {
        return mDatabase;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

}
