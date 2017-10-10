package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

import lombok.Getter;
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
}
