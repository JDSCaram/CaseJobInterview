package com.entrevista.ifood2.presentation.ui.cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.presentation.presenter.cart.CartPresenter;
import com.entrevista.ifood2.presentation.presenter.cart.CartPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.cart.CartView;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantAndProducts;
import com.entrevista.ifood2.toolbox.AlertDialogBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartView {

    private View mEmptyView;
    private View mContainerView;
    private CartPresenter mPresenter;

    private SimpleDraweeView mRestaurantImage;
    private TextView mName, mDescription, mDeliveryFee, mDeliveryFeeTotal, mAmountTotal;
    private RatingBar mRatingBar;
    private Button mBtMethodPayment;

    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private AlertDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Layout General
        mEmptyView = findViewById(R.id.empty_view);
        mContainerView = findViewById(R.id.constraintLayout);

        //Layout Restaurant
        mRestaurantImage = findViewById(R.id.restaurant_image);
        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);
        mDeliveryFee = findViewById(R.id.delivery_fee);
        mDeliveryFeeTotal = findViewById(R.id.delivery_fee_total);
        mRatingBar = findViewById(R.id.rating_bar);
        mAmountTotal = findViewById(R.id.value_total);
        mBtMethodPayment = findViewById(R.id.bt_method_payment);
        mRecyclerView = findViewById(R.id.recycler_view_products);


        mPresenter = new CartPresenterImpl(RepositoryImpl.getInstance(new LocalData(), new RemoteData()));
        mPresenter.setView(this);

        afterViews();
    }

    private void afterViews() {
        mAdapter = new CartAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getProducts();
    }

    @Override
    public void showProgress() {
        if (mProgress == null)
            mProgress = AlertDialogBuilder.alertDialogProgress(this, R.layout.progress_dialog);
        mProgress.show();
    }

    @Override
    public void hideProgress() {
        if (mProgress != null)
            mProgress.dismiss();
    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(this).setMessage(message).create().show();
    }

    @Override
    public void showErrorMessage() {
        new AlertDialog.Builder(this).setMessage(getString(R.string.error)).create().show();
    }

    @Override
    public void showErrorMessage(String msg) {
        new AlertDialog.Builder(this).setMessage(msg).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @UiThread
    @Override
    public void loadProducts(RestaurantAndProducts restaurantAndProducts) {
        if (CollectionUtils.isEmpty(restaurantAndProducts.products)) {
            mEmptyView.setVisibility(View.VISIBLE);
            mContainerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mContainerView.setVisibility(View.VISIBLE);
        }
        mAdapter.updateList(restaurantAndProducts.products);
    }
}
