package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

/**
 * Created by JCARAM on 07/10/2017.
 */

public class Restaurant implements Serializable{
    private long id;
    private String name;
    private String imageUrl;
    private String description;
    private String address;
    private double rating;
    private double deliveryFee;

    public Restaurant() {
    }

    public Restaurant(long id, String name, String imageUrl, String description, String address, double rating, double deliveryFee) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.deliveryFee = deliveryFee;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
