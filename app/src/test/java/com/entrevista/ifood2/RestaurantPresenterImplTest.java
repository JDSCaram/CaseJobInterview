package com.entrevista.ifood2;

import android.app.Application;

import com.entrevista.ifood2.network.ServiceMapper;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantPresenter;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantPresenterImpl;
import com.entrevista.ifood2.presentation.presenter.restaurants.RestaurantView;
import com.entrevista.ifood2.repository.Repository;
import com.entrevista.ifood2.repository.RepositoryImpl;
import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;
import com.entrevista.ifood2.toolbox.AssetsUtils;
import com.google.gson.GsonBuilder;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by JCARAM on 11/10/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RestaurantPresenterImplTest {

    @Mock
    Restaurant mRestaurant;

    @Mock
    RestaurantView mView;

    @Mock
    LocalData localData;

    @Mock
    RemoteData remoteData;

    @Mock
    ServiceMapper serviceMapper;

    private Restaurant mResponse;
    private RestaurantPresenter mRestaurantPresenter;
    private RepositoryImpl mRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Application application = RuntimeEnvironment.application;
        Assert.assertNotNull(application);

        mRepository = RepositoryImpl.getInstance(localData, remoteData);
        when(remoteData.getServices()).thenReturn(serviceMapper);

        mRestaurantPresenter = new RestaurantPresenterImpl(mRepository);
        mRestaurantPresenter.setView(mView);

        String json = AssetsUtils.readFromfile(application, "json/restaurant.json");
        mResponse = new GsonBuilder().create().fromJson(json, Restaurant.class);
        Assert.assertNotNull(mResponse);
    }

    @Test
    public void getRestaurantTest(){

        verify(serviceMapper).getRestaurants(-22.3748873,-47.5572083);
        verify(mView).showProgress();
    }
}
