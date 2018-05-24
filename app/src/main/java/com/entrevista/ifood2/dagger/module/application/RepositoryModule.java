package com.entrevista.ifood2.dagger.module.application;

import com.entrevista.ifood2.repository.Repository;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {Created by Jonatas Caram on 21/05/2018.
 */
@Module
public class RepositoryModule {
    @Singleton
    @Provides
    public Repository provideRepository(LocalData localData, RemoteData remoteData) {
        return new RepositoryImpl(localData, remoteData);
    }
}
