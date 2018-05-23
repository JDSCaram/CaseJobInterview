package com.entrevista.ifood2.dagger.module;

import com.entrevista.ifood2.dagger.scope.PerFragmentScope;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jonatas Caram on 23/05/2018
 */
@Module
public class RestaurantModule {
    private RestaurantView mView;

    public RestaurantModule(RestaurantView mView) {
        this.mView = mView;
    }

    @Provides
    @PerFragmentScope
    RestaurantView provideRestaurantView(){
        return mView;
    }
}
