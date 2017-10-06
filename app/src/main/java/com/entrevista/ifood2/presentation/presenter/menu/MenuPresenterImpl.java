package com.entrevista.ifood2.presentation.presenter.menu;

import android.support.annotation.NonNull;

import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantInteractorView;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class MenuPresenterImpl implements MenuPresenter {


    @Override
    public void setView(@NonNull RestaurantInteractorView view) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isViewAttached() {
        return false;
    }
}
