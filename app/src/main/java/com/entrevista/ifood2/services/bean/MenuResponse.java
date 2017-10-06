package com.entrevista.ifood2.services.bean;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 06/10/2017.
 */
@Getter @Setter
public class MenuResponse implements Serializable {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private int price;
}
