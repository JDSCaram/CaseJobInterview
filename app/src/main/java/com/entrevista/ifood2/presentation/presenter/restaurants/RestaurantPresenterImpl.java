package com.entrevista.ifood2.presentation.presenter.restaurants;


import android.support.annotation.NonNull;


import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.repository.Repository;
import com.entrevista.ifood2.repository.RepositoryImpl;

import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class RestaurantPresenterImpl implements RestaurantPresenter {

    private RestaurantView mView;
    public Repository repository;

    public RestaurantPresenterImpl(RepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void setView(@NonNull RestaurantView view) {
        mView = view;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getRestaurants(double latitude, double longitude) {
        if (!isViewAttached()) return;
        mView.showProgress();

        Observable<List<Restaurant>> observable = repository.beginRemote().getServices().getRestaurants(latitude, longitude);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Restaurant> restaurantsResponse) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        mView.showListRestaurants(restaurantsResponse);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (e instanceof UnknownHostException) {
                            mView.showTryAgain();
                        } else {
                            mView.showErrorMessage(e.getMessage());
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
