package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by JCARAM on 10/10/2017.
 */
@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRestaurant(Restaurant restaurant);

    @Query("SELECT * FROM Restaurant")
    Maybe<RestaurantAndProducts> getProductsForRestaurant();

    @Query("SELECT * FROM Restaurant")
    Maybe<List<Restaurant>> getAllRestaurant();


    @Query("DELETE FROM Restaurant")
    int deleteAllRestaurants();

    @Delete
    int deleteRestaurantById(Restaurant restaurant);

    @Update
    int updateRestaurantById(Restaurant restaurant);
}
