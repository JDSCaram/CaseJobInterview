package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by JCARAM on 09/10/2017.
 */

@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class Restaurant {

    @PrimaryKey
    public int id;

    public String name;
    public String imageUrl;
    public String description;
    public String address;
    public double rating;
    public double deliveryFee;

    @Ignore
    List<Product> products;

    public Restaurant() {
    }

    private Restaurant(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setImageUrl(builder.imageUrl);
        setDescription(builder.description);
        setAddress(builder.address);
        setRating(builder.rating);
        setDeliveryFee(builder.deliveryFee);
        setProducts(builder.products);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public static final class Builder {
        private int id;
        private String name;
        private String imageUrl;
        private String description;
        private String address;
        private double rating;
        private double deliveryFee;
        private List<Product> products;

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

        public Builder imageUrl(String val) {
            imageUrl = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder rating(double val) {
            rating = val;
            return this;
        }

        public Builder deliveryFee(double val) {
            deliveryFee = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
