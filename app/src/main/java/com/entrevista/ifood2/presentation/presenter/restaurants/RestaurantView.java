package com.entrevista.ifood2.presentation.presenter.restaurants;

import com.entrevista.ifood2.presentation.presenter.BaseView;
import com.entrevista.ifood2.network.bean.Restaurant;

import java.util.List;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface RestaurantView extends BaseView {
    //TODO: METODOS DE RETORNO PARA A VIEW
    void showListRestaurants(List<Restaurant> restaurants);

}
