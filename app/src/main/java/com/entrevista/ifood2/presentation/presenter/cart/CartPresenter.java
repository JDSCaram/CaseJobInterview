package com.entrevista.ifood2.presentation.presenter.cart;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.presentation.presenter.BasePresenter;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;

import java.util.List;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface CartPresenter extends BasePresenter<CartView> {
    void getProducts();

    void getMethodPayments();

    void checkout(PaymentMethod mCurrentMethod, Restaurant mCurrentRestaurant, List<Product> mCurrentProducts);

    void cleanCart();

    void removeProduct(Product item);

    void updateItemCart(Product product);
}
