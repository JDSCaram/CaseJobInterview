package com.entrevista.ifood2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.ProductDao;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantDao;
import com.entrevista.ifood2.storage.database.AppDataBase;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private AppDataBase mDatabase;
    private RestaurantDao mRestaurantDao;
    private ProductDao mProductDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDataBase.class)
                .allowMainThreadQueries().build();
        mRestaurantDao = mDatabase.restaurantDao();
        mProductDao = mDatabase.productDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }


    @Test
    public void noContainsRestaurants() {
        //When there is no user in the database and the query returns no rows, 'Maybe' will complete.
        mRestaurantDao.getAllRestaurant().test().assertComplete();
    }

    @Test
    public void insertRestaurantAndProduct() {
        List<Product> products = buildProducts();
        Restaurant restaurant = new Restaurant.Builder()
                .id(1)
                .address("Test Address")
                .deliveryFee(10)
                .description("The Best Restaurant")
                .imageUrl("http://www.festbossajazz.com.br/wp-content/uploads/2016/08/Logomarca-1.png")
                .name("Test Restaurant Name")
                .rating(10)
                .products(products)
                .build();

        products.get(0).setRestaurantId(restaurant.getId());

        Long nRestaurantPersis = mRestaurantDao.insertRestaurant(restaurant);
        Long nProduct = mProductDao.insertProduct(products.get(0));

        Assert.assertTrue(nProduct > 0 && nRestaurantPersis > 0);
    }


    private List<Product> buildProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            products.add(new Product.Builder()
                    .id(i)
                    .amount(10 + i * 2)
                    .description("Description")
                    .quantity(1 + i)
                    .unitAmount(25)
                    .name("Test Name product")
                    .urlImage("")
                    .build());
        }
        return products;
    }

    private Product buildProduct() {
        return new Product.Builder()
                .id(1)
                .amount(10 + 2)
                .description("Description")
                .quantity(1)
                .unitAmount(25)
                .name("Test Name product")
                .urlImage("")
                .build();

    }


}
