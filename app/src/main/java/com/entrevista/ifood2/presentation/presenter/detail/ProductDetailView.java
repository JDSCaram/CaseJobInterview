package com.entrevista.ifood2.presentation.presenter.detail;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.presentation.presenter.BaseView;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface ProductDetailView extends BaseView {
    void productAddToCartSuccess();

    void alreadyExists(CheckoutRequest checkoutRequest);

    void successCleanCart();
}
