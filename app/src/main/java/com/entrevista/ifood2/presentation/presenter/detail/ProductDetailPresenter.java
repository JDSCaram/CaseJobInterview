package com.entrevista.ifood2.presentation.presenter.detail;

import com.entrevista.ifood2.presentation.presenter.BasePresenter;
import com.entrevista.ifood2.network.bean.CheckoutRequest;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface ProductDetailPresenter extends BasePresenter<ProductDetailView> {
    void addToCard(CheckoutRequest checkoutRequest);

    void cleanCart(CheckoutRequest checkoutRequest);
}
