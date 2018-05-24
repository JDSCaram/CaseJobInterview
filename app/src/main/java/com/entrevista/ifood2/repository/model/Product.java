package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by JCARAM on 09/10/2017.
 */

@Entity(indices = {@Index(value = {"id"}, unique = true)},
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                parentColumns = "id",
                childColumns = "restaurantId"))
public class Product {

    @PrimaryKey
    public int id;

    public String name;
    public int quantity;
    public double amount;

    @ColumnInfo(name = "url_image")
    public String urlImage;

    public double unitAmount;
    public String description;
    public int restaurantId;

    public Product(int id, String name, int quantity, double amount, double unitAmount,
                   String urlImage, String description, int restaurantId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
        this.unitAmount = unitAmount;
        this.urlImage = urlImage;
        this.description = description;
        this.restaurantId = restaurantId;
    }

    private Product(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setQuantity(builder.quantity);
        setAmount(builder.amount);
        setUrlImage(builder.urlImage);
        setUnitAmount(builder.unitAmount);
        setDescription(builder.description);
        setRestaurantId(builder.restaurantId);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }


    public static final class Builder {
        private int id;
        private String name;
        private int quantity;
        private double amount;
        private String urlImage;
        private double unitAmount;
        private String description;
        private int restaurantId;

        public Builder() {
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder amount(double val) {
            amount = val;
            return this;
        }

        public Builder urlImage(String val) {
            urlImage = val;
            return this;
        }

        public Builder unitAmount(double val) {
            unitAmount = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder restaurantId(int val) {
            restaurantId = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
