package com.entrevista.ifood2.dagger.module;

import com.entrevista.ifood2.dagger.scope.PerActivityScope;
import com.entrevista.ifood2.presentation.presenter.detail.ProductDetailView;

import dagger.Module;
import dagger.Provides;

/**
 * {Created by Jonatas Caram on 17/05/2018}.
 */
@Module
public class DetailModule {
    private ProductDetailView mView;

    public DetailModule(ProductDetailView mView) {
        this.mView = mView;
    }

    @Provides
    @PerActivityScope
    ProductDetailView provideProductDetailView() {
        return mView;
    }
}
