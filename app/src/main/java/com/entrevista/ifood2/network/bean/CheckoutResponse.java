package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 06/10/2017.
 */
@Getter @Setter
public class CheckoutResponse implements Serializable{
    private String status;
    private String message;
}
