package com.entrevista.ifood2.presentation.presenter.restaurants;


import android.support.annotation.NonNull;


import com.entrevista.ifood2.services.ServiceMapper;
import com.entrevista.ifood2.services.bean.Restaurant;

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

public class RestaurantPresenterImpl implements RestaurantPresenter{

    private RestaurantView mView;
    public ServiceMapper serviceMapper;

    public RestaurantPresenterImpl(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
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

        Observable<List<Restaurant>> observable  = serviceMapper.getRestaurants(latitude, longitude);
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

                        if (restaurantsResponse != null)
                            mView.showListRestaurants(restaurantsResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (e instanceof UnknownHostException) {
                            mView.showErrorMessage("Por favor verifique a sua conexão.");
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
