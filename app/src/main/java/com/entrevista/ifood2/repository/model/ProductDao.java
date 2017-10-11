package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by JCARAM on 10/10/2017.
 */
@Dao
public interface ProductDao {

    @Insert
    long insertProduct(Product product);

    @Query("SELECT * FROM Product")
    List<Product> getAllProduct();

    @Query("SELECT * FROM Product WHERE id = :id LIMIT 1")
    Single<Product> getProductById(int id);

    @Query("DELETE FROM Product")
    int deleteAllProducts();

    @Delete
    int deleteProductById(Product product);

    @Update
    int updateProductById(Product product);
}
