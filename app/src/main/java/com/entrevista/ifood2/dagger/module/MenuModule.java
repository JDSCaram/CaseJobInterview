package com.entrevista.ifood2.dagger.module;

import com.entrevista.ifood2.dagger.scope.PerFragmentScope;
import com.entrevista.ifood2.presentation.presenter.menu.MenuView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Jonatas Caram on 23/05/2018.
 * for Bode's Lab in www.bodeslab.com
 */
@Module
public class MenuModule {
    private MenuView mView;

    public MenuModule(MenuView mView) {
        this.mView = mView;
    }

    @Provides
    @PerFragmentScope
    MenuView provideMenuView() {
        return mView;
    }
}
