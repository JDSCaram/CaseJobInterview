package com.entrevista.ifood2.network.bean;


import java.io.Serializable;

/**
 * Created by JCARAM on 07/10/2017.
 */
public class Menu implements Serializable {
    private long id;
    private String name;
    private String imageUrl;
    private String description;
    private double price;
    private int quantity;

    public Menu() {
    }

    public Menu(long id, String name, String imageUrl, String description, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
