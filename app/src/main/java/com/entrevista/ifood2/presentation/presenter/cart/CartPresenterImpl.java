package com.entrevista.ifood2.presentation.presenter.cart;

import android.support.annotation.NonNull;
import android.util.Log;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.CheckoutResponse;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantAndProducts;

import java.net.UnknownHostException;
import java.util.ArrayList;
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

        Maybe<RestaurantAndProducts> observable = repository.beginLocal().getDatabase().restaurantDao().getProductsForRestaurant();
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<RestaurantAndProducts>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull RestaurantAndProducts restaurantAndProducts) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.loadProducts(restaurantAndProducts);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.loadProducts(new RestaurantAndProducts());
                    }
                });

    }

    @Override
    public void getMethodPayments() {
        mView.showProgress();


        repository.beginRemote().getServices().getPaymentMethods()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PaymentMethod>>() {
                               @Override
                               public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                               }

                               @Override
                               public void onNext(@io.reactivex.annotations.NonNull List<PaymentMethod> paymentMethods) {
                                   if (!isViewAttached()) return;
                                   mView.hideProgress();
                                   mView.loadMethodPayments(paymentMethods);
                               }

                               @Override
                               public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                   if (!isViewAttached()) return;
                                   mView.hideProgress();
                                   mView.showErrorMessage(e.getMessage());

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }

    @Override
    public void checkout(PaymentMethod mCurrentMethod, Restaurant mCurrentRestaurant,
                         List<Product> mCurrentProducts) {

        if (!isViewAttached()) return;
        mView.showProgress();

        CheckoutRequest request = new CheckoutRequest();
        request.setRestaurant(initRestaurantRequest(mCurrentRestaurant));
        request.setMenus(iniProductRequest(mCurrentProducts));
        request.setMethod(mCurrentMethod);

        repository.beginRemote().getServices().checkout(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckoutResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull CheckoutResponse checkoutResponse) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (checkoutResponse.getStatus().contains("SUCCESS")) {
                            mView.checkoutSuccess(checkoutResponse);
                        } else {
                            mView.showErrorMessage();
                        }
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

    @Override
    public void cleanCart() {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return repository.beginLocal().getDatabase().productDao().deleteAllProducts();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Produtos: " + integer);
                    }
                });

        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return repository.beginLocal().getDatabase().restaurantDao().deleteAllRestaurants();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Restaurante: " + integer);
                        mView.successCleanCart();
                    }
                });
    }

    @Override
    public void removeProduct(final Product item) {
        mView.showProgress();
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return repository.beginLocal().getDatabase().productDao().deleteProductById(item);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        mView.hideProgress();
                        Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Produtos: " + integer);
                        if(integer > 0 )
                            mView.updateUi();
                    }
                });

    }

    @Override
    public void updateItemCart(final Product product) {
        mView.showProgress();

        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return repository.beginLocal().getDatabase().productDao().updateProductById(product);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        mView.hideProgress();
                    }
                });

    }


    private com.entrevista.ifood2.network.bean.Restaurant initRestaurantRequest(Restaurant restaurant) {
        com.entrevista.ifood2.network.bean.Restaurant restaurantRequest
                = new com.entrevista.ifood2.network.bean.Restaurant();
        restaurantRequest.setId(restaurant.getId());
        restaurantRequest.setRating(restaurant.getRating());
        restaurantRequest.setAddress(restaurant.getAddress());
        restaurantRequest.setDeliveryFee(restaurant.getDeliveryFee());
        restaurantRequest.setDescription(restaurant.getDescription());
        restaurantRequest.setImageUrl(restaurant.getImageUrl());
        restaurantRequest.setName(restaurant.getName());
        return restaurantRequest;
    }


    private List<Menu> iniProductRequest(List<Product> products) {
        List<Menu> menus = new ArrayList<>();
        for (Product p : products) {
            menus.add(new Menu(p.getId(), p.getName(), p.getUrlImage(),
                    p.getDescription(), p.getAmount(), p.getQuantity()));
        }
        return menus;
    }

}
