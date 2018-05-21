package com.entrevista.ifood2.repository.data;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by JCARAM on 09/10/2017.
 */

public class RemoteData {

    private Retrofit mRetrofit;

    @Inject
    public RemoteData(Retrofit mRetrofit) {
        this.mRetrofit = mRetrofit;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
