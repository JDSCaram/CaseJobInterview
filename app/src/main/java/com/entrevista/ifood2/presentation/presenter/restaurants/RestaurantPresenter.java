package com.entrevista.ifood2.presentation.presenter.restaurants;

import com.entrevista.ifood2.presentation.presenter.BasePresenter;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface RestaurantPresenter extends BasePresenter<RestaurantView> {
    //TODO: METODOS USADOS DENTRO DO PRESENTER
    void getRestaurants(double latitude, double longitude);
}
