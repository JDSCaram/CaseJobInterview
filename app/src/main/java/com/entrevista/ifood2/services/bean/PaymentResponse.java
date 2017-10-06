package com.entrevista.ifood2.services.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 05/10/2017.
 */
@Getter @Setter
public class PaymentResponse implements Serializable{
    public int id;
    public String name;
    public boolean enabled;
}
