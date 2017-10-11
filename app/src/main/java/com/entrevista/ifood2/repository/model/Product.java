package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 09/10/2017.
 */
@Getter
@Setter
@Entity(indices = {@Index(value = {"id"}, unique = true)},
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                parentColumns = "id",
                childColumns = "restaurantId"))
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    public int id;
    public String name;
    public int quantity;
    public double amount;

    @ColumnInfo(name = "url_image")
    public String urlImage;

    public double unitAmount;

    public int restaurantId;

    public Product(int id, String name, int quantity, double amount, double unitAmount, String urlImage, int restaurantId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
        this.unitAmount = unitAmount;
        this.urlImage = urlImage;
        this.restaurantId = restaurantId;
    }


}
