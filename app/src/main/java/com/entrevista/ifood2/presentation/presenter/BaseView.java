package com.entrevista.ifood2.presentation.presenter;


/**
 * Created by JCARAM on 05/10/2017.
 */

public interface BaseView<T extends BaseView>{
    void showProgress();
    void hideProgress();
    void showMessage(String message);
    void showErrorMessage();
    void showErrorMessage(String msg);
}
