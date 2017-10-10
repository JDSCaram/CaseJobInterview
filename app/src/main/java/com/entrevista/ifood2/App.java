package com.entrevista.ifood2;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.entrevista.ifood2.storage.database.AppDataBase;
import com.facebook.drawee.backends.pipeline.Fresco;

import lombok.Getter;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class App extends Application {

    public static App INSTANCE;
    private static final String DATABASE_NAME = "ifood_db";
    private AppDataBase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        initDatabase();
    }

    private void initDatabase() {
        mDatabase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public AppDataBase getDatabase() {
        return mDatabase;
    }

    public static App getInstance() {
        return INSTANCE;
    }

}
