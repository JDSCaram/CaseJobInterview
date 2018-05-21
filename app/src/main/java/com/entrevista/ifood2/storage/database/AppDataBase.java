package com.entrevista.ifood2.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.ProductDao;
import com.entrevista.ifood2.repository.model.Restaurant;
import com.entrevista.ifood2.repository.model.RestaurantDao;

/**
 * Created by JCARAM on 09/10/2017.
 */
@Database(entities = {Restaurant.class, Product.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "ifood_db";

    public abstract RestaurantDao restaurantDao();

    public abstract ProductDao productDao();
}
