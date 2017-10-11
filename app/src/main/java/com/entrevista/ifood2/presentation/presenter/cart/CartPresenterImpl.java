package com.entrevista.ifood2.presentation.presenter.cart;

import android.support.annotation.NonNull;
import android.util.Log;

import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantAndProducts;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class CartPresenterImpl implements CartPresenter {

    private static final String TAG_LOG_PRESENTER = CartPresenterImpl.class.getSimpleName();

    private CartView mView;
    private RepositoryImpl repository;

    public CartPresenterImpl(RepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void setView(@NonNull CartView view) {
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
    public void getProducts() {
        if (!isViewAttached())
            return;

        mView.showProgress();

        Maybe<RestaurantAndProducts> observable =  repository.beginLocal().getDatabase().restaurantDao().getProductsForRestaurant();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<RestaurantAndProducts>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull RestaurantAndProducts restaurantAndProducts) {
                        mView.hideProgress();
                        mView.loadProducts(restaurantAndProducts);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        mView.loadProducts(new RestaurantAndProducts());
                    }
                });

    }

//    private void getProducts(final Restaurant restaurants){
//
//        Observable<List<Product>> observable = Observable.fromCallable(new Callable<List<Product>>() {
//            @Override
//            public List<Product> call() throws Exception {
////                return repository.beginLocal().getDatabase().productDao().getProductsForRestaurant(restaurants.get(0).getUid());
//                return repository.beginLocal().getDatabase().productDao().getAllProduct();
//            }
//        });
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Product>>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.annotations.NonNull List<Product> products) {
//                        mView.hideProgress();
//                        if (CollectionUtils.isNotEmpty(products))
//                            mView.loadProducts(products);
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                        mView.hideProgress();
//                        Log.e(TAG_LOG_PRESENTER, "onError: ", e );
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//
//    }
}
