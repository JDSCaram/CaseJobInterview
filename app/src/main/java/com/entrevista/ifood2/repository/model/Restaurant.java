package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 09/10/2017.
 */
@Getter
@Setter
@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public int id;

    public String name;
    public String imageUrl;
    public String description;
    public String address;
    public double rating;
    public double deliveryFee;

    @Ignore
    List<Product> products;
}
