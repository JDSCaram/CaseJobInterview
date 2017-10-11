package com.entrevista.ifood2.presentation.presenter.cart;

import com.entrevista.ifood2.network.bean.CheckoutResponse;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.presentation.presenter.BaseView;
import com.entrevista.ifood2.repository.model.RestaurantAndProducts;

import java.util.List;


/**
 * Created by JCARAM on 05/10/2017.
 */

public interface CartView extends BaseView {
    void loadProducts(RestaurantAndProducts restaurantAndProducts);

    void loadMethodPayments(List<PaymentMethod> paymentMethods);


    void checkoutSuccess(CheckoutResponse checkoutResponse);

    void successCleanCart();

    void updateUi();
}
