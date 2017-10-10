package com.entrevista.ifood2.network.bean;




import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;


/**
 * Created by JCARAM on 07/10/2017.
 */
@Setter @Getter
public class CheckoutRequest implements Parcelable {
    private Restaurant restaurant;
    private PaymentMethod method;
    private List<Menu> menus;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.restaurant);
        dest.writeSerializable(this.method);
        dest.writeList(this.menus);
    }

    public CheckoutRequest() {
    }

    protected CheckoutRequest(Parcel in) {
        this.restaurant = (Restaurant) in.readSerializable();
        this.method = (PaymentMethod) in.readSerializable();
        this.menus = new ArrayList<Menu>();
        in.readList(this.menus, Menu.class.getClassLoader());
    }

    public static final Creator<CheckoutRequest> CREATOR = new Creator<CheckoutRequest>() {
        @Override
        public CheckoutRequest createFromParcel(Parcel source) {
            return new CheckoutRequest(source);
        }

        @Override
        public CheckoutRequest[] newArray(int size) {
            return new CheckoutRequest[size];
        }
    };
}
