package com.entrevista.ifood2.presentation.presenter.cart;

import android.support.annotation.NonNull;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class CartPresenterImpl implements CartPresenter {

    // TODO: 05/10/2017 : Carrinho deve ser persistido

    @Override
    public void setView(@NonNull CartView view) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isViewAttached() {
        return false;
    }
}
