package com.entrevista.ifood2.presentation.presenter;

import android.support.annotation.NonNull;

/**
 * Created by JCARAM on 05/10/2017.
 */

public interface BasePresenter<T extends BaseView>  {
    void setView(@NonNull T view);
    void onDestroy();
    boolean isViewAttached();
}
