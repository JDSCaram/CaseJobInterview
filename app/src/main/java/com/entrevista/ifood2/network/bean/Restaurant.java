package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by JCARAM on 07/10/2017.
 */

@Getter @Setter
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
}
