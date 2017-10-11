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
}
