package com.entrevista.ifood2.presentation.ui.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.presentation.presenter.detail.ProductDetailPresenter;
import com.entrevista.ifood2.presentation.presenter.detail.ProductDetailPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.detail.ProductDetailView;
import com.entrevista.ifood2.presentation.ui.cart.CartActivity;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;
import com.entrevista.ifood2.toolbox.AlertDialogBuilder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements ProductDetailView {

    private static final int QUANTITY_MIN = 1;
    private CheckoutRequest mCheckout;
    private SimpleDraweeView mImage;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mName, mDescription, mQuantity, mValue, mUnitValue;
    private Button mAddCart;
    private ImageButton mAdd, mRemove;
    private BigDecimal mValues;
    private ProductDetailPresenter mPresenter;
    private AlertDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        mCheckout = i.getParcelableExtra(CheckoutRequest.class.getSimpleName());

        mImage = findViewById(R.id.image);
        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);
        mQuantity = findViewById(R.id.quantity);
        mValue = findViewById(R.id.value);
        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);
        mAddCart = findViewById(R.id.add_cart);
        AppBarLayout mAppbarLayout = findViewById(R.id.app_bar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mUnitValue = findViewById(R.id.unit_value);

        mPresenter = new ProductDetailPresenterImpl(RepositoryImpl.getInstance(new LocalData(), new RemoteData()));
        mPresenter.setView(this);

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    mToolbarLayout.setTitle(mCheckout.getRestaurant().getName());
                } else {
                    mToolbarLayout.setTitle("");
                }
            }
        });

        afterViews();
    }

    private void afterViews() {

        if (!TextUtils.isEmpty(mCheckout.getMenus().get(0).getImageUrl())) {
            Uri uri = Uri.parse(mCheckout.getMenus().get(0).getImageUrl());
            DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri).build();
            mImage.setController(controller);
        }
        if (!TextUtils.isEmpty(mCheckout.getMenus().get(0).getName())) {
            mName.setText(mCheckout.getMenus().get(0).getName());
        }
        if (!TextUtils.isEmpty(mCheckout.getMenus().get(0).getDescription())) {
            mDescription.setText(mCheckout.getMenus().get(0).getDescription());
        }
        double unitValueFixo = mCheckout.getMenus().get(0).getPrice();
        mValues = BigDecimal.valueOf(mCheckout.getMenus().get(0).getPrice());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        mValue.setText(format.format(mCheckout.getMenus().get(0).getPrice()));
        mUnitValue.setText(format.format(unitValueFixo));
        mCheckout.getMenus().get(0).setQuantity(QUANTITY_MIN);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mQuantity.getText().toString());
                calculeValue(quantity + 1);
            }
        });

        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mQuantity.getText().toString());
                calculeValue(quantity - 1);
            }
        });

        mAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

    }

    private void addToCart() {
        if (mCheckout != null)
            mPresenter.addToCard(mCheckout);
    }

    @UiThread
    private void calculeValue(final int quantity) {
        mCheckout.getMenus().get(0).setQuantity(quantity);
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        BigDecimal amount = incrementValue(quantity);
        mCheckout.getMenus().get(0).setPrice(amount.doubleValue());
        final String value = format.format(amount);
        mValue.setText(value);
        if (quantity <= 1)
            mQuantity.setText(String.valueOf(1));
        else {
            mQuantity.setText(String.valueOf(quantity));
        }
    }

    private BigDecimal incrementValue(int count) {
        BigDecimal quantity = (BigDecimal.valueOf(count));
        if (quantity.doubleValue() > BigDecimal.ONE.doubleValue())
            return mValues.multiply(quantity);
        else
            return mValues.multiply(BigDecimal.ONE);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void productAddToCartSuccess() {
        Toast.makeText(this, R.string.product_add_to_cart, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void alreadyExists(final CheckoutRequest checkoutRequest) {
        AlertDialogBuilder.alertDialogWithButtonConfirmAndCancel(this,
                getString(R.string.already_exists),
                getString(R.string.clear),
                getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.cleanCart(checkoutRequest);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public void successCleanCart() {
        Toast.makeText(this, R.string.success_clean_cart, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.cart: {
                Intent i = new Intent(DetailActivity.this, CartActivity.class);
                startActivity(i);
                return false;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ifood_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);
        View view = menuItem.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }
}
