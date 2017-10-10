package com.entrevista.ifood2.repository;

import android.support.annotation.NonNull;

import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;

/**
 * Created by JCARAM on 09/10/2017.
 */

public class RepositoryImpl implements Repository {

    private static RepositoryImpl INSTANCE = null;
    private LocalData mLocalData;
    private RemoteData mRemoteData;

    public RepositoryImpl(@NonNull LocalData localData, @NonNull RemoteData remoteData) {
        mLocalData = localData;
        mRemoteData = remoteData;
    }

    @Override
    public LocalData beginLocal() {
        return mLocalData;
    }

    @Override
    public RemoteData beginRemote() {
        return mRemoteData;
    }

    public static RepositoryImpl getInstance(LocalData localData, RemoteData remoteData) {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryImpl(localData, remoteData);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
