package com.entrevista.ifood2.repository;

import com.entrevista.ifood2.repository.data.LocalData;
import com.entrevista.ifood2.repository.data.RemoteData;

/**
 * Created by JCARAM on 09/10/2017.
 */

public interface Repository {
    LocalData beginLocal();
    RemoteData beginRemote();
}
