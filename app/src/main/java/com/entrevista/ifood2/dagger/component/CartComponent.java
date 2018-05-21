package com.entrevista.ifood2.dagger.component;

import com.entrevista.ifood2.dagger.module.CartModule;
import com.entrevista.ifood2.dagger.scope.PerActivityScope;
import com.entrevista.ifood2.presentation.ui.cart.CartActivity;

import dagger.Component;

/**
 * {Created by Jonatas Caram on 21/05/2018}.
 */
@PerActivityScope
@Component(dependencies = AppComponent.class,
        modules = CartModule.class)
public interface CartComponent {
    void inject(CartActivity activity);
}
