package com.entrevista.ifood2.presentation.presenter.cart;

import android.util.Log;

import com.entrevista.ifood2.network.ServiceMapper;
import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.repository.Repository;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantAndProducts;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class CartPresenterImpl implements CartPresenter {

    private static final String TAG_LOG_PRESENTER = CartPresenterImpl.class.getSimpleName();

    private CartView mView;
    private Repository repository;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public CartPresenterImpl(Repository repository, CartView view) {
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
    public void getProducts() {
        if (!isViewAttached())
            return;

        mView.showProgress();

        Maybe<RestaurantAndProducts> observable = repository.beginLocal().getDatabase().restaurantDao().getProductsForRestaurant();
        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::returnToView, error -> {
                            if (!isViewAttached()) return;
                            mView.hideProgress();
                        }, () -> returnToView(new RestaurantAndProducts())));

    }

    private void returnToView(RestaurantAndProducts restaurantAndProducts) {
        if (!isViewAttached()) return;
        mView.hideProgress();
        mView.loadProducts(restaurantAndProducts);
    }

    @Override
    public void getMethodPayments() {
        mView.showProgress();

        mCompositeDisposable.add(
                repository.beginRemote().getRetrofit().create(ServiceMapper.class).getPaymentMethods()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(paymentMethods -> {
                            if (!isViewAttached()) return;
                            mView.hideProgress();
                            mView.loadMethodPayments(paymentMethods);
                        }, error -> {
                            if (!isViewAttached()) return;
                            mView.hideProgress();
                            mView.showErrorMessage(error.getMessage());
                        }));
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

        mCompositeDisposable.add(
                repository.beginRemote().getRetrofit().create(ServiceMapper.class)
                        .checkout(request)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(checkoutResponse -> {
                            if (!isViewAttached()) return;
                            mView.hideProgress();

                            if (checkoutResponse.getStatus().contains("SUCCESS")) {
                                mView.checkoutSuccess(checkoutResponse);
                            } else {
                                mView.showErrorMessage();
                            }
                        }, error -> {
                            if (!isViewAttached()) return;
                            mView.hideProgress();
                            if (error instanceof UnknownHostException) {
                                mView.showTryAgain();
                            } else {
                                mView.showErrorMessage(error.getMessage());
                            }
                        }));
    }


    @Override
    public void cleanCart() {
        mCompositeDisposable.add(Observable.fromCallable(() -> repository.beginLocal().getDatabase().productDao().deleteAllProducts()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Produtos: " + integer)));

        mCompositeDisposable.add(Observable.fromCallable(() -> repository.beginLocal().getDatabase().restaurantDao().deleteAllRestaurants()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Restaurante: " + integer);
                    mView.successCleanCart();
                }));
    }

    @Override
    public void removeProduct(final Product item) {
        mView.showProgress();
        mCompositeDisposable.add(
                Observable.fromCallable(() -> repository.beginLocal().getDatabase().productDao().deleteProductById(item)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            mView.hideProgress();
                            Log.i(TAG_LOG_PRESENTER, "Numero de linhas apagadas em Produtos: " + integer);
                            if (integer > 0)
                                mView.updateUi();
                        }));

    }

    @Override
    public void updateItemCart(final Product product) {
        mView.showProgress();
        mCompositeDisposable.add(
                Observable.fromCallable(() -> repository.beginLocal().getDatabase().productDao().updateProductById(product)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> mView.hideProgress()));

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
