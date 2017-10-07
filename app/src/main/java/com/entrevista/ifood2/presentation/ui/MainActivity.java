package com.entrevista.ifood2.presentation.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.presentation.ui.cart.CartActivity;
import com.entrevista.ifood2.presentation.ui.restaurants.RestaurantFragment;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachFragment(new RestaurantFragment(), RestaurantFragment.class.getSimpleName());
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
        if(manager.getBackStackEntryCount() <= 1 ) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ifood_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
                return false;
            }
        });
        return super.onOptionsItemSelected(item);
    }
}
