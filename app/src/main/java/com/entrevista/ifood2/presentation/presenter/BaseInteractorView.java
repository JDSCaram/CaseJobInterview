package com.entrevista.ifood2.presentation.presenter;


/**
 * Created by JCARAM on 05/10/2017.
 */

public interface BaseInteractorView<T extends BaseInteractorView>{
    void showProgress();
    void hideProgress();
    void showMessage(String message);
    void showErrorMessage();
    void showErrorMessage(String msg);
}
