package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 07/10/2017.
 */
@Getter @Setter
public class PaymentMethod implements Serializable{
    private int id;
    private String name;
    private boolean enabled;
}
