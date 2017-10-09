package com.entrevista.ifood2.presentation.presenter.detail;

import android.support.annotation.NonNull;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class ProductDetailPresenterImpl implements ProductDetailPresenter {


    private ProductDetailView mView;

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
}
