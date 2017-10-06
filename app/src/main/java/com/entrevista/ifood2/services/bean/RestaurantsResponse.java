package com.entrevista.ifood2.services.bean;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 06/10/2017.
 */
@Getter @Setter
public class RestaurantsResponse implements Serializable{
    public int id;
    public String name;
    public String imageUrl;
    public String description;
    public String address;
    public double rating;
    public int deliveryFee;
}
