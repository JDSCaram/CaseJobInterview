package com.entrevista.ifood2.repository;

import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;

import javax.inject.Inject;

/**
 * Created by JCARAM on 09/10/2017.
 */

public class RepositoryImpl implements Repository {

    private LocalData mLocalData;
    private RemoteData mRemoteData;

    @Inject
    public RepositoryImpl(LocalData localData, RemoteData remoteData) {
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

}
