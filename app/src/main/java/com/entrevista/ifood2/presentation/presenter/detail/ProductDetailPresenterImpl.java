package com.entrevista.ifood2.presentation.presenter.detail;

import android.arch.persistence.room.EmptyResultSetException;
import android.util.Log;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class ProductDetailPresenterImpl implements ProductDetailPresenter {

    private static final String TAG_LOG_PRESENTER = ProductDetailPresenterImpl.class.getSimpleName();
    private ProductDetailView mView;
    private RepositoryImpl repository;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public ProductDetailPresenterImpl(RepositoryImpl repository, ProductDetailView mView) {
        this.mView = mView;
        this.repository = repository;
        mCompositeDisposable = new CompositeDisposable();
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
    public void addToCard(final CheckoutRequest checkoutRequest) {
        if (!isViewAttached())
            return;

        mView.showProgress();
        //Quando não há nenhum usuario no banco a consulta nao retorna nada Maybe vai pra concluida
        //Quando existe um usuario no banco, vai pra onSuccess

        Maybe<List<Restaurant>> observable = repository.beginLocal().getDatabase().restaurantDao().getAllRestaurant();
        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(restaurants -> {
                            if (isSameRestaurantOrEmpty(restaurants, checkoutRequest)) {
                                insertRestaurantAndProduct(checkoutRequest);
                            } else {
                                mView.hideProgress();
                                mView.alreadyExists(checkoutRequest);
                            }
                        }, onError -> {
                            Log.e(TAG_LOG_PRESENTER, "addToCard: ", onError);
                        }, () -> insertRestaurantAndProduct(checkoutRequest)));


    }

    @Override
    public void cleanCart(CheckoutRequest checkoutRequest) {

        mCompositeDisposable.add(Observable.fromCallable(() ->
                repository.beginLocal().getDatabase()
                        .productDao()
                        .deleteAllProducts())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Produtos: " + integer);
                    mView.successCleanCart();
                }));


    }


    private void insertProductOrUpdate(final Product product, final Restaurant restaurant) {
        Single<Product> observable = repository.beginLocal().getDatabase().productDao().getProductById(product.getId());
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productBase -> {
                    updateProduct(product, productBase);
                }, onError -> {
                    if (onError instanceof EmptyResultSetException)
                        createNewProduct(product, restaurant);
                }));
    }

    private void updateProduct(final Product newProduct, final Product oldProduct) {

        int quantity = newProduct.getQuantity() + oldProduct.getQuantity();
        double amount = newProduct.getAmount() + oldProduct.getAmount();
        newProduct.setQuantity(quantity);
        newProduct.setAmount(amount);
        newProduct.setUid(oldProduct.getUid());

        mCompositeDisposable.add(Observable.fromCallable(() ->
                repository.beginLocal().getDatabase()
                        .productDao()
                        .updateProductById(newProduct))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    mView.hideProgress();
                    if (integer > 0)
                        mView.productAddToCartSuccess();
                }));

    }

    private void createNewProduct(final Product product, final Restaurant restaurant) {
        Observable<Long> observable = Observable.fromCallable(() ->
                repository.beginLocal()
                        .getDatabase()
                        .restaurantDao()
                        .insertRestaurant(restaurant));

        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            Observable.fromCallable(() -> repository.beginLocal()
                                    .getDatabase()
                                    .productDao()
                                    .insertProduct(product))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(al -> {
                                        Log.i(TAG_LOG_PRESENTER, "onNext: N: " + aLong);
                                        mView.hideProgress();
                                        if (aLong > 0)
                                            mView.productAddToCartSuccess();
                                    }, onError -> {
                                        mView.hideProgress();
                                        Log.e(TAG_LOG_PRESENTER, "onError: ", onError);
                                    });
                        }, onError -> {
                            mView.hideProgress();
                            Log.e(TAG_LOG_PRESENTER, "onError: ", onError);
                        }));

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
