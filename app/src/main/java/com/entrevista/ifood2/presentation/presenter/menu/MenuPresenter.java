package com.entrevista.ifood2.presentation.presenter.menu;

import com.entrevista.ifood2.presentation.presenter.BasePresenter;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface MenuPresenter extends BasePresenter<MenuView> {
    void getMenus(long id);
}
