package com.entrevista.ifood2.dagger.component;

import com.entrevista.ifood2.dagger.module.RestaurantModule;
import com.entrevista.ifood2.dagger.scope.PerFragmentScope;
import com.entrevista.ifood2.presentation.ui.restaurants.RestaurantFragment;

import dagger.Component;

/**
 * Created by Jonatas Caram on 23/05/2018.
 * for Bode's Lab in www.bodeslab.com
 */
@PerFragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = RestaurantModule.class
)
public interface RestaurantComponent {
    void inject(RestaurantFragment fragment);
}
