package com.entrevista.ifood2.presentation.presenter.detail;

import android.arch.persistence.room.EmptyResultSetException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        List<Restaurant> restaurants;
        //Quando não há nenhum usuario no banco a consulta nao retorna nada Maybe vai pra concluida
        //Quando existe um usuario no banco, vai pra onSuccess

        Maybe<List<Restaurant>> observable = repository.beginLocal().getDatabase().restaurantDao().getAllRestaurant();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<Restaurant>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<Restaurant> restaurants) {
                        if (isSameRestaurantOrEmpty(restaurants, checkoutRequest)) {
                            insertRestaurantAndProduct(checkoutRequest);
                        } else {
                            mView.hideProgress();
                            mView.alreadyExists(checkoutRequest);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        insertRestaurantAndProduct(checkoutRequest);
                    }
                });


    }

    @Override
    public void cleanCart(CheckoutRequest checkoutRequest) {

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

                        cleanRestaurant();
                    }
                });


    }

    private void cleanRestaurant() {
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


    private void insertProductOrUpdate(final Product product, final Restaurant restaurant) {
        Single<Product> observable = repository.beginLocal().getDatabase().productDao().getProductById(product.getId());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Product>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Product productBase) {
                        updateProduct(product, productBase);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (e instanceof EmptyResultSetException)
                            createNewProduct(product, restaurant);
                    }
                });
    }

    private void updateProduct(final Product newProduct, final Product oldProduct) {

        int quantity = newProduct.getQuantity() + oldProduct.getQuantity();
        double amount = newProduct.getAmount() + oldProduct.getAmount();
        newProduct.setQuantity(quantity);
        newProduct.setAmount(amount);
        newProduct.setUid(oldProduct.getUid());

        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return repository.beginLocal().getDatabase().productDao().updateProductById(newProduct);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                        mView.hideProgress();
                        if (integer > 0)
                            mView.productAddToCartSuccess();
                    }
                });

    }

    private void createNewProduct(final Product product, final Restaurant restaurant) {
        Observable<Long> observable = Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return repository.beginLocal().getDatabase().restaurantDao().insertRestaurant(restaurant);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                        Observable.fromCallable(new Callable<Long>() {
                            @Override
                            public Long call() throws Exception {
                                return repository.beginLocal().getDatabase().productDao().insertProduct(product);
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                                    }

                                    @Override
                                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                                        Log.i(TAG_LOG_PRESENTER, "onNext: N: " + aLong);
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


    private boolean isSameRestaurantOrEmpty(List<Restaurant> restaurants, CheckoutRequest checkoutRequest) {
        if (!restaurants.isEmpty()) {
            for (Restaurant restaurant : restaurants) {
                if (restaurant.getId() == checkoutRequest.getRestaurant().getId())
                    return true;
            }
        } else {
            return true;
        }
        return false;
    }


    private void insertRestaurantAndProduct(CheckoutRequest checkoutRequest) {
        if (!checkoutRequest.getMenus().isEmpty()) {
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
                        item.getQuantity() == 1 ? item.getPrice() : (item.getPrice() / item.getQuantity()), //valor original
                        item.getImageUrl(),
                        item.getDescription(),
                        restaurant.getId()));
            }

            insertProductOrUpdate(products.get(0), restaurant);

        }
    }

}
