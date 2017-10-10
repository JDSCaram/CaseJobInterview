package com.entrevista.ifood2.presentation.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.presentation.presenter.menu.MenuPresenter;
import com.entrevista.ifood2.presentation.presenter.menu.MenuPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.menu.MenuView;
import com.entrevista.ifood2.presentation.ui.MainActivity;
import com.entrevista.ifood2.presentation.ui.detail.DetailActivity;
import com.entrevista.ifood2.network.ServiceFactory;
import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;
import com.entrevista.ifood2.toolbox.AlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class MenuFragment extends Fragment implements MenuView, MenuAdapter.OnMenuClickListener {

    private RecyclerView mRecyclerView;
    private MenuAdapter mAdapter;
    private Restaurant currentRestaurant;
    private MenuPresenter mPresenter;
    private AlertDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mPresenter = new MenuPresenterImpl(RepositoryImpl.getInstance(new LocalData(), new RemoteData())); //inicializa o mPresenter
        afterViews();
        return view;
    }

    private void afterViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new MenuAdapter(getContext());
        mAdapter.setOnMenuClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentRestaurant = (Restaurant) bundle.getSerializable(Restaurant.class.getSimpleName());
            mPresenter.setView(this);
            mPresenter.getMenus(currentRestaurant.getId());
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
            mProgress = AlertDialogBuilder.alertDialogProgress(getContext(), R.layout.progress_dialog);
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
}
