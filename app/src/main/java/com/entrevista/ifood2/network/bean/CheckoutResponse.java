package com.entrevista.ifood2.network.bean;

import java.io.Serializable;

/**
 * Created by JCARAM on 06/10/2017.
 */
public class CheckoutResponse implements Serializable{
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
