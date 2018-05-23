package com.entrevista.ifood2.presentation.presenter.restaurants;


import com.entrevista.ifood2.network.ServiceMapper;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.repository.Repository;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class RestaurantPresenterImpl implements RestaurantPresenter {

    private RestaurantView mView;
    public Repository repository;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public RestaurantPresenterImpl(Repository repository, RestaurantView view) {
        this.repository = repository;
        this.mView = view;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mCompositeDisposable.clear();
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getRestaurants(double latitude, double longitude) {
        if (!isViewAttached()) return;
        mView.showProgress();

        Observable<List<Restaurant>> observable = repository.beginRemote()
                .getRetrofit().create(ServiceMapper.class)
                .getRestaurants(latitude, longitude);

        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::returnRestaurantToView, this::returnErrorToView));

    }

    private void returnErrorToView(Throwable onError) {
        if (!isViewAttached()) return;
        mView.hideProgress();

        if (onError instanceof UnknownHostException) {
            mView.showTryAgain();
        } else {
            mView.showErrorMessage(onError.getMessage());
        }
    }

    private void returnRestaurantToView(List<Restaurant> restaurants) {
        if (!isViewAttached()) return;
        mView.hideProgress();

        mView.showListRestaurants(restaurants);
    }


}
