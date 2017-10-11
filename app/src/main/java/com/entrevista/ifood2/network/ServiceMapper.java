package com.entrevista.ifood2.network;

import com.entrevista.ifood2.network.bean.CheckoutRequest;
import com.entrevista.ifood2.network.bean.CheckoutResponse;
import com.entrevista.ifood2.network.bean.Menu;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.network.bean.Restaurant;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by JCARAM on 06/10/2017.
 */

public interface ServiceMapper {
    @Headers({
            "Content-type: application/json"
    })
    //     https://demo1531977.mockable.io/restaurants?lat=-22.3748873&lng=-47.5572083
    //GET: https://demo1531977.mockable.io/restaurants?lat={latitude}&lng={longitude}
    //GET: https://demo1531977.mockable.io/menu/{restaurantId}
    //GET: https://demo1531977.mockable.io/payments
    //POST: https://demo1531977.mockable.io/checkout

    @GET("restaurants")
    Observable<List<Restaurant>> getRestaurants(@Query("lat") double lat, @Query("lng") double lng);

    @GET("menu/{restaurantId}")
    Observable<List<Menu>> getMenu(@Path("restaurantId") long restaurantId);

    @GET("payments")
    Observable<List<PaymentMethod>> getPaymentMethods();

    @POST("checkout")
    Observable<CheckoutResponse> checkout(@Body CheckoutRequest request);

}
