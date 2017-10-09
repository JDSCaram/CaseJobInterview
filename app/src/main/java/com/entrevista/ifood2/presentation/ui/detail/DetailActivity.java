package com.entrevista.ifood2.presentation.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.services.bean.CheckoutRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private CheckoutRequest mCheckout;
    private SimpleDraweeView mImage;
    private AppBarLayout mAppbarLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private Toolbar mToolbar;
    private TextView mName, mDescription, mQuantity, mValue, mUnitValue;
    private Button mAddCart;
    private ImageButton mAdd, mRemove;
    private BigDecimal mValues;
    private double unitValueFixo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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
        mAppbarLayout = findViewById(R.id.app_bar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mUnitValue = findViewById(R.id.unit_value);

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

        if (StringUtils.isNotBlank(mCheckout.getMenus().get(0).getImageUrl())) {
            Uri uri = Uri.parse(mCheckout.getMenus().get(0).getImageUrl());
            DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri).build();
            mImage.setController(controller);
        }
        if (StringUtils.isNotBlank(mCheckout.getMenus().get(0).getName())) {
            mName.setText(mCheckout.getMenus().get(0).getName());
        }
        if (StringUtils.isNotBlank(mCheckout.getMenus().get(0).getDescription())) {
            mDescription.setText(mCheckout.getMenus().get(0).getDescription());
        }
        unitValueFixo = mCheckout.getMenus().get(0).getPrice();
        mValues = BigDecimal.valueOf(mCheckout.getMenus().get(0).getPrice());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        mValue.setText(format.format(mCheckout.getMenus().get(0).getPrice()));
        mUnitValue.setText(format.format(unitValueFixo));

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

    }

    private void calculeValue(final int quantity) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        final String value = format.format(incrementValue(quantity));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mValue.setText(value);
                if (quantity <= 1)
                    mQuantity.setText(String.valueOf(1));
                else {
                    mQuantity.setText(String.valueOf(quantity));
                }
            }
        });

    }

    private BigDecimal incrementValue(int count) {
        BigDecimal quantity = (BigDecimal.valueOf(count));
        if (quantity.doubleValue() > BigDecimal.ONE.doubleValue())
            return mValues.multiply(quantity);
        else
            return mValues.multiply(BigDecimal.ONE);
    }


}
