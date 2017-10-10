package com.entrevista.ifood2.presentation.ui;

import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.entrevista.ifood2.R;
import com.entrevista.ifood2.presentation.ui.cart.CartActivity;
import com.entrevista.ifood2.presentation.ui.restaurants.RestaurantFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final int PERMISSIONS_REQUEST_LOCATION = 1500;
    private static final long INTERVAL_MILLISECONDS = 10000;

    private FragmentManager manager;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    @Getter
    private Location mCurrentLocation;
    private TextView cartBadge;
    private int mValueBadge = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleApiClient();
        if (mCurrentLocation != null)
            attachFragment(new RestaurantFragment(), getString(R.string.title_restaurant));

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_content);
                setActionBarTitle(fragment.getTag());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void attachFragment(Fragment fragment, String tag) {
        manager = getSupportFragmentManager();
        manager.findFragmentByTag(tag);
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) { // Sem fragment no backstack com mesmo nome.
            ft.add(R.id.frame_content, fragment, tag);
            ft.addToBackStack(tag);
            ft.commit();
        } else {
            ft.show(manager.findFragmentByTag(tag)).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() <= 1) { // Quando so tiver um fragment no backStack
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ifood_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);
        View view = menuItem.getActionView();
        cartBadge = view.findViewById(R.id.cart_badge);
        initBadge();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart: {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @UiThread
    private void initBadge() {
        if (cartBadge != null) {
            if (mValueBadge == 0) {
                if (cartBadge.getVisibility() != View.GONE) {
                    cartBadge.setVisibility(View.GONE);
                }
            } else {
                cartBadge.setText(String.valueOf(Math.min(mValueBadge, 99)));
                if (cartBadge.getVisibility() != View.VISIBLE) {
                    cartBadge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }


    public void showSnackbar(int textId, final int actionStringId, View.OnClickListener listener) {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view,
                getString(textId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void requestPermissions() {
        boolean permissionRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionRationale) {
            showSnackbar(R.string.gps_permission, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length <= 0) {
                Log.e(MainActivity.class.getSimpleName(), "pedido de permissao cancelado");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLocation() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mCurrentLocation == null) {
                    mCurrentLocation = location;
                    attachFragment(new RestaurantFragment(), getString(R.string.title_restaurant));
                } else {
                    mCurrentLocation = location;
                }

            }
        });

    }

    protected synchronized void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL_MILLISECONDS);
        mLocationRequest.setFastestInterval(INTERVAL_MILLISECONDS / 5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    public void setActionBarTitle(String title) {
        if (StringUtils.isNotBlank(title) && getSupportActionBar() != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(title);
        }
    }
}
