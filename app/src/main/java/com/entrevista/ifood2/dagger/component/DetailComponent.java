package com.entrevista.ifood2.dagger.component;

import com.entrevista.ifood2.dagger.module.DetailModule;
import com.entrevista.ifood2.dagger.scope.PerActivityScope;
import com.entrevista.ifood2.presentation.presenter.detail.ProductDetailView;
import com.entrevista.ifood2.presentation.ui.detail.DetailActivity;

import dagger.Component;

/**
 * {Created by Jonatas Caram on 21/05/2018}.
 */
@PerActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = DetailModule.class
)
public interface DetailComponent {
    void inject(DetailActivity activity);
}
