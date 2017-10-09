package com.entrevista.ifood2.presentation.presenter.menu;

import com.entrevista.ifood2.presentation.presenter.BaseView;
import com.entrevista.ifood2.services.bean.Menu;

import java.util.List;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface MenuView extends BaseView {
    void loadMenus(List<Menu> menus);
}
