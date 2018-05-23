package com.entrevista.ifood2.presentation.presenter.menu;

import com.entrevista.ifood2.network.ServiceMapper;
import com.entrevista.ifood2.network.bean.Menu;
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

public class MenuPresenterImpl implements MenuPresenter {

    private MenuView mView;
    private Repository repository;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public MenuPresenterImpl(Repository repository, MenuView view) {
        this.repository = repository;
        this.mView = view;
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
    public void getMenus(long id) {
        if (!isViewAttached()) return;
        mView.showProgress();

        Observable<List<Menu>> observable = repository.beginRemote()
                .getRetrofit().create(ServiceMapper.class)
                .getMenu(id);

        mCompositeDisposable.add(
                observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::returnMenuToView, this::returnErrorToView));

    }

    private void returnErrorToView(Throwable e) {
        if (!isViewAttached()) return;
        mView.hideProgress();

        if (e instanceof UnknownHostException) {
            mView.showTryAgain();
        } else {
            mView.showErrorMessage(e.getMessage());
        }
    }

    private void returnMenuToView(List<Menu> menus) {
        if (!isViewAttached()) return;
        mView.hideProgress();

        if (menus != null)
            mView.loadMenus(menus);
    }
}
