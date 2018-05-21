package com.entrevista.ifood2.dagger.module.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.entrevista.ifood2.storage.database.AppDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {Created by Jonatas Caram on 21/05/2018}.
 */
@Module
public class RoomModule {
    private AppDataBase mDatabase;

    public RoomModule(Application application) {
        mDatabase = Room.databaseBuilder(application,
                AppDataBase.class, AppDataBase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    AppDataBase providesRoomDatabase(){
        return mDatabase;
    }


}
