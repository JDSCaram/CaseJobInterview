package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by JCARAM on 10/10/2017.
 */
@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProduct(Product... products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertProduct(Product product);

    @Query("SELECT * FROM Product")
    public List<Product> getAllProduct();

    @Query("SELECT * FROM Product WHERE id = :id LIMIT 1")
    public Product getProductById(int id);

    @Query("DELETE FROM Product")
    int deleteAllProducts();

    @Delete
    int deleteProductById(Product product);

    @Update
    int updateProductById(Product product);
}
