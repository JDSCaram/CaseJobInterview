package com.entrevista.ifood2.repository.data;

import com.entrevista.ifood2.network.ServiceFactory;
import com.entrevista.ifood2.network.ServiceMapper;

/**
 * Created by JCARAM on 09/10/2017.
 */

public class RemoteData {
    private ServiceMapper factory;

    public ServiceMapper getServices(){
        if (factory == null)
            factory = ServiceFactory.makeService();
        return factory;
    }
}
