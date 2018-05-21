package com.entrevista.ifood2.dagger.module.application;

import com.entrevista.ifood2.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * {Created by Jonatas Caram on 21/05/2018}.
 */
@Module
public class RetrofitModule {
    private static final int HTTP_READ_TIMEOUT = 540;
    private static final int HTTP_CONNECT_TIMEOUT = 540;

    private String mBaseURL;

    public RetrofitModule(String mBaseURL) {
        this.mBaseURL = mBaseURL;
    }


    @Singleton
    @Provides
    Retrofit providesRetrofit(OkHttpClient okHttpClient,
                              GsonConverterFactory gsonConverterFactory) {

        return new Retrofit.Builder()
                .baseUrl(mBaseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public GsonConverterFactory providesGsonConverterFactory() {
        return GsonConverterFactory.create();
    }



    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
        return logging;
    }


    @Singleton
    @Provides
    Dispatcher provideDispatcher() {
        return new Dispatcher();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Dispatcher dispatcher,
                                     HttpLoggingInterceptor logginInterceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();
        httpClientBuilder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(logginInterceptor);
        httpClientBuilder.dispatcher(dispatcher);
        return httpClientBuilder.build();
    }
}
