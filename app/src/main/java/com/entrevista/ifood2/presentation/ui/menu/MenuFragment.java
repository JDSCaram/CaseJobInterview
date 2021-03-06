package com.entrevista.ifood2.presentation.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.entrevista.ifood2.App;
import com.entrevista.ifood2.R;
import com.entrevista.ifood2.dagger.component.DaggerMenuComponent;
import com.entrevista.ifood2.dagger.component.MenuComponent;
import com.entrevista.ifood2.dagger.module.MenuModule;
import com.entrevista.ifood2.presentation.presenter.menu.MenuPresenter;
import com.entrevista.ifood2.presentation.presenter.menu.MenuPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.menu.MenuView;
import com.entrevista.ifood2.presentation.ui.MainActivity;
import com.entrevista.ifood2.presentation.ui.detail.DetailActivity;
import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;
import com.entrevista.ifood2.toolbox.AlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class MenuFragment extends Fragment implements MenuView, MenuAdapter.OnMenuClickListener {

    private RecyclerView mRecyclerView;
    private MenuAdapter mAdapter;
    private Restaurant currentRestaurant;
    private AlertDialog mProgress;

    @Inject
    MenuPresenterImpl mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        initComponent();
        afterViews();
        return view;
    }

    private void initComponent() {
        DaggerMenuComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getAppComponent())
                .menuModule(new MenuModule(this))
                .build()
                .inject(this);
    }

    private void afterViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new MenuAdapter(getContext());
        mAdapter.setOnMenuClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        getMenu();

    }

    private void getMenu() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentRestaurant = (Restaurant) bundle.getSerializable(Restaurant.class.getSimpleName());
            mPresenter.getMenus(currentRestaurant != null ? currentRestaurant.getId() : 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_menu));
    }

    @Override
    public void showProgress() {
        if (mProgress == null)
            mProgress = AlertDialogBuilder.alertDialogProgress(getContext(), R.layout.dialog_progress);
        mProgress.show();
    }

    @Override
    public void hideProgress() {
        if (mProgress != null)
            mProgress.dismiss();
    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(getContext()).setMessage(message).create().show();
    }

    @Override
    public void showErrorMessage() {
        new AlertDialog.Builder(getContext()).setMessage(getString(R.string.error)).create().show();
    }

    @Override
    public void showErrorMessage(String msg) {
        new AlertDialog.Builder(getContext()).setMessage(msg).create().show();
    }

    @Override
    public void showTryAgain() {
        AlertDialogBuilder.alertDialogTryAgain(getContext(),
                getString(R.string.error_connection),
                getString(R.string.try_again),
                getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getMenu();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    public void loadMenus(List<Menu> menus) {
        mAdapter.updateMenus(menus);
    }


    @Override
    public void onClickItem(final Menu item) {

        CheckoutRequest checkout = new CheckoutRequest();
        checkout.setMenus(new ArrayList<Menu>() {{
            add(item);
        }});
        checkout.setRestaurant(currentRestaurant);

        Intent i = new Intent(getContext(), DetailActivity.class);
        i.putExtra(CheckoutRequest.class.getSimpleName(), checkout);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
