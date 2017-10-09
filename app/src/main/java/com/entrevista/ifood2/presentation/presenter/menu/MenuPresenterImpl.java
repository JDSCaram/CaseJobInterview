package com.entrevista.ifood2.presentation.presenter.menu;

import android.support.annotation.NonNull;

import com.entrevista.ifood2.services.ServiceMapper;
import com.entrevista.ifood2.services.bean.Menu;

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

public class MenuPresenterImpl implements MenuPresenter {

    private MenuView mView;
    private ServiceMapper serviceMapper;


    public MenuPresenterImpl(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    @Override
    public void setView(@NonNull MenuView view) {
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
    public void getMenus(long id) {
        if (!isViewAttached()) return;
        mView.showProgress();

        Observable<List<Menu>> observable = serviceMapper.getMenu(id);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Menu>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Menu> menuResponses) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();

                        if (menuResponses != null)
                            mView.loadMenus(menuResponses);
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
