package com.entrevista.ifood2.presentation.presenter.cart;

import com.entrevista.ifood2.presentation.presenter.BaseView;
import com.entrevista.ifood2.repository.model.Product;

import java.util.List;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface CartView extends BaseView {
    void loadProducts(List<Product> products);
}
