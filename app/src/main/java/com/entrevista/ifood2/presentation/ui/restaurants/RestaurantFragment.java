package com.entrevista.ifood2.presentation.ui.restaurants;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entrevista.ifood2.App;
import com.entrevista.ifood2.R;
import com.entrevista.ifood2.dagger.component.DaggerRestaurantComponent;
import com.entrevista.ifood2.dagger.module.RestaurantModule;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantView;
import com.entrevista.ifood2.presentation.ui.MainActivity;
import com.entrevista.ifood2.presentation.ui.menu.MenuFragment;
import com.entrevista.ifood2.toolbox.AlertDialogBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class RestaurantFragment extends Fragment implements RestaurantView, RestaurantAdapter.OnRestaurantClickListener {

    private Location mCurrentLocation;
    private RecyclerView mRecyclerView;
    private RestaurantAdapter mAdapter;
    private AlertDialog mProgress;
    private View mEmptyView;

    @Inject
    RestaurantPresenterImpl mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyView = view.findViewById(R.id.empty_view);

        Bundle bundle = getArguments();
        if (bundle != null)
            mCurrentLocation = bundle.getParcelable("LOCATION");

        initComponent();
        afterViews();
        return view;
    }

    private void initComponent() {

        DaggerRestaurantComponent.builder()
                .appComponent(((App) getActivity().getApplication()).getAppComponent())
                .restaurantModule(new RestaurantModule(this))
                .build().inject(this);
    }

    private void afterViews() {
        mAdapter = new RestaurantAdapter(getContext());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        if (mCurrentLocation != null)
            mPresenter.getRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        else
            ((MainActivity) getActivity()).setLocationListener(location -> {
                mCurrentLocation = location;
                mPresenter.getRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_restaurant));
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
                (dialogInterface, i) -> {
                    if (mCurrentLocation != null)
                        mPresenter.getRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    getActivity().finish();
                }).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    @Override
    public void showListRestaurants(List<Restaurant> restaurants) {
        if (restaurants.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.loadRestaurants(restaurants);
        }

    }


    @Override
    public void onClickItem(Restaurant item) {
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Restaurant.class.getSimpleName(), item);
            MenuFragment menuFragment = new MenuFragment();
            menuFragment.setArguments(bundle);
            ((MainActivity) getActivity()).attachFragment(menuFragment, getString(R.string.title_menu)); //Abre o Menu passando os dados do Restaurante
        }
    }

}
