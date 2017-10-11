package com.entrevista.ifood2.presentation.ui.cart;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.CheckoutResponse;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.PaymentMethod;
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
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartView, CartAdapter.OnProductItemClick, View.OnClickListener {

    private View mEmptyView, mContainerView, mDeliveryLayout;
    private CartPresenter mPresenter;

    private SimpleDraweeView mRestaurantImage;
    private TextView mName, mDescription, mDeliveryFeeTotal, mAmountTotal;
    private RatingBar mRatingBar;
    private Button mBtMethodPayment;

    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private AlertDialog mProgress;
    private BigDecimal mTotalAmount, mDeliveryFee;
    private List<Product> mCurrentProducts;
    private Restaurant mCurrentRestaurant;
    private PaymentMethod mCurrentMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Layout General
        mEmptyView = findViewById(R.id.empty_view);
        mContainerView = findViewById(R.id.constraintLayout);

        //Layout Restaurant
        mRestaurantImage = findViewById(R.id.restaurant_image);
        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);
        mDeliveryFeeTotal = findViewById(R.id.delivery_fee_total);
        mRatingBar = findViewById(R.id.rating_bar);
        mAmountTotal = findViewById(R.id.value_total);
        mBtMethodPayment = findViewById(R.id.bt_method_payment);
        mRecyclerView = findViewById(R.id.recycler_view_products);
        mDeliveryLayout = findViewById(R.id.delivery_fee_container);

        mDeliveryLayout.setVisibility(View.GONE);
        mPresenter = new CartPresenterImpl(RepositoryImpl.getInstance(new LocalData(), new RemoteData()));
        mPresenter.setView(this);
        mBtMethodPayment.setOnClickListener(this);
        afterViews();
    }

    private void afterViews() {
        mAdapter = new CartAdapter(this);
        mAdapter.setOnProductItemClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getProducts();
    }

    @Override
    public void showProgress() {
        if (mProgress == null)
            mProgress = AlertDialogBuilder.alertDialogProgress(this, R.layout.dialog_progress);
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
    public void showTryAgain() {
        AlertDialogBuilder.alertDialogTryAgain(this,
                getString(R.string.error_connection),
                getString(R.string.try_again),
                getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkout();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
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
            initRestaurantData(restaurantAndProducts.restaurant);
            mDeliveryFee = BigDecimal.valueOf(restaurantAndProducts.restaurant.getDeliveryFee());
            updateTotal(restaurantAndProducts.products);

        }
        mAdapter.updateList(restaurantAndProducts.products);

    }


    @UiThread
    private void initRestaurantData(Restaurant restaurant) {
        mCurrentRestaurant = restaurant;
        if (StringUtils.isNotBlank(restaurant.getImageUrl()))
            mRestaurantImage.setImageURI(restaurant.getImageUrl());

        if (StringUtils.isNotBlank(restaurant.getName()))
            mName.setText(restaurant.getName());

        if (StringUtils.isNotBlank(restaurant.getDescription()))
            mDescription.setText(restaurant.getDescription());

        mRatingBar.setRating((float) restaurant.getRating());
        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.star_yellow), PorterDuff.Mode.SRC_ATOP);

    }


    @UiThread
    @Override
    public void updateItem(final List<Product> products, final Product item, final int position, int increment) {
        if (increment <= 0) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.remove_product))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            products.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            mPresenter.removeProduct(item);
                        }
                    }).setNegativeButton(getString(R.string.no), null).create().show();

        } else {
            item.setQuantity(increment);
            item.setAmount(item.getUnitAmount() * increment);
            mPresenter.updateItemCart(item);
        }
        products.set(position, item);
        updateTotal(products);
        mAdapter.notifyItemChanged(position, item);
    }

    @UiThread
    public void updateTotal(List<Product> products) {
        mCurrentProducts = products;
        mTotalAmount = BigDecimal.ZERO;
        mDeliveryFee = BigDecimal.ZERO;
        for (Product item : products)
            mTotalAmount = mTotalAmount.add(BigDecimal.valueOf(item.getAmount()));
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        mDeliveryFee = BigDecimal.valueOf(mCurrentRestaurant.getDeliveryFee());
        mAmountTotal.setText(format.format(mTotalAmount.add(mDeliveryFee)));
        mDeliveryFeeTotal.setText(format.format(mDeliveryFee));

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_method_payment) {
            mPresenter.getMethodPayments();
        }
    }


    @Override
    public void loadMethodPayments(final List<PaymentMethod> paymentMethods) {
        if (CollectionUtils.isNotEmpty(paymentMethods)) {
            for (int i = 0; i < paymentMethods.size(); i++) {
                if (!paymentMethods.get(i).isEnabled())
                    paymentMethods.remove(i);
            }

            showDialogMethodPayments(paymentMethods);
        } else

        {
            showErrorMessage(getString(R.string.no_method_payment));
        }

    }


    @UiThread
    private void showDialogMethodPayments(final List<PaymentMethod> paymentMethods) {
        AlertDialogBuilder.alertDialogCustomPaymentMethod(this,
                getString(R.string.method_payment),
                paymentMethods,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkout();
                    }
                },
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("MethodPayment", "onItemSelected: " + paymentMethods.get(i).getName());
                        mCurrentMethod = paymentMethods.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }).show();
    }

    private void checkout() {
        if (mCurrentMethod != null)
            mPresenter.checkout(mCurrentMethod, mCurrentRestaurant, mCurrentProducts);
    }


    @Override
    public void checkoutSuccess(CheckoutResponse checkoutResponse) {
        new AlertDialog.Builder(this).setMessage(checkoutResponse.getMessage())
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.cleanCart();
                    }
                }).create().show();
    }

    @Override
    public void successCleanCart() {
        finish();
    }

    @Override
    public void updateUi() {
        mPresenter.getProducts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
