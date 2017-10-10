package com.entrevista.ifood2.repository.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by JCARAM on 10/10/2017.
 */

public class RestaurantAndProducts {

    @Embedded
    public Restaurant restaurant ;

    @Relation(parentColumn = "id", entityColumn = "restaurantId", entity = Product.class)
    public List<Product> products;
}
