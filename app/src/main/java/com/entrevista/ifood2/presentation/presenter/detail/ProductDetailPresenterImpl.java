package com.entrevista.ifood2.presentation.presenter.detail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class ProductDetailPresenterImpl implements ProductDetailPresenter {

    private static final String TAG_LOG_PRESENTER = ProductDetailPresenterImpl.class.getSimpleName();
    private ProductDetailView mView;
    private RepositoryImpl repository;

    public ProductDetailPresenterImpl(RepositoryImpl instance) {
        repository = instance;
    }

    @Override
    public void setView(@NonNull ProductDetailView view) {
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
    public void addToCard(final CheckoutRequest checkoutRequest) {
        if (!isViewAttached())
            return;

        mView.showProgress();

//        Single<Restaurant> single = repository.beginLocal().getDatabase().restaurantDao()
//                .getRestaurantByIdObservable((int) checkoutRequest.getRestaurant().getId());
//
//        single.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Restaurant>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(@io.reactivex.annotations.NonNull Restaurant restaurant) {
//                        if (restaurant != null)
//                            insertRestaurant(checkoutRequest);
//                        else {
//                            mView.hideProgress();
//                            mView.showMessage("Opa! Voce ja tem um produto de outro restaurante em seu carrinho.");
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                        insertRestaurant(checkoutRequest);
//
//                    }
//                });


    }

    private void insertRestaurant(CheckoutRequest checkoutRequest) {
        if (CollectionUtils.isNotEmpty(checkoutRequest.getMenus())) {
            final List<Product> products = new ArrayList<>();
            final Restaurant restaurant = new Restaurant();
            restaurant.setAddress(checkoutRequest.getRestaurant().getAddress());
            restaurant.setDescription(checkoutRequest.getRestaurant().getDescription());
            restaurant.setDeliveryFee(checkoutRequest.getRestaurant().getDeliveryFee());
            restaurant.setId((int) checkoutRequest.getRestaurant().getId());
            restaurant.setName(checkoutRequest.getRestaurant().getName());
            restaurant.setImageUrl(checkoutRequest.getRestaurant().getImageUrl());
            restaurant.setRating(checkoutRequest.getRestaurant().getRating());

            for (Menu item : checkoutRequest.getMenus()) {
                products.add(new Product(
                        (int) item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getImageUrl(),
                        restaurant.getId()));
            }

            Observable<Long> observableProduct = Observable.fromCallable(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return repository.beginLocal().getDatabase().productDao().insertProduct(products.get(0));
                }
            });

            observableProduct.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                            Log.i(TAG_LOG_PRESENTER, "onNext: N: " + aLong);
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            Log.e(TAG_LOG_PRESENTER, "onError: ", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG_LOG_PRESENTER, "onComplete: ");
                        }
                    });


            Observable<Long> observable = Observable.fromCallable(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return repository.beginLocal().getDatabase().restaurantDao().insertRestaurant(restaurant);
                }
            });
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                            mView.hideProgress();
                            if (aLong > 0)
                                mView.productAddToCartSuccess();

                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            mView.hideProgress();
                            Log.e(TAG_LOG_PRESENTER, "onError: ", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG_LOG_PRESENTER, "onComplete: ");
                        }
                    });


        }
    }


    public void insertMenuForRestaurantInCart(final Restaurant restaurant, List<Product> products) {
        for (Product item : products) {
            item.setRestaurantId(restaurant.getId());
        }

    }
}
