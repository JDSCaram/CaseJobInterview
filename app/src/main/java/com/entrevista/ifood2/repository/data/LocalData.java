package com.entrevista.ifood2.repository.data;


import com.entrevista.ifood2.App;
import com.entrevista.ifood2.storage.database.AppDataBase;


/**
 * Created by JCARAM on 09/10/2017.
 */

public class LocalData {
    private AppDataBase mDatabase;

    public AppDataBase getDatabase() {
        if (mDatabase == null)
            mDatabase = App.getInstance().getDatabase();

        return mDatabase;
    }


}
