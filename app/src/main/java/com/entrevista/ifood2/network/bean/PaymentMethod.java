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
public class PaymentMethod implements Serializable{
    private int id;
    private String name;
    private boolean enabled;

    public PaymentMethod() {
    }

    public PaymentMethod(int id, String name, boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }
}
