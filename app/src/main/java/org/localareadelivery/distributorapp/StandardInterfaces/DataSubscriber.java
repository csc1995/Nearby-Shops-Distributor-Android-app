package org.localareadelivery.distributorapp.StandardInterfaces;

import java.util.List;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DataSubscriber {

    public interface CreateCallback<T> {

        public void createCallback(boolean isOffline,boolean isSuccessful,int httpStatusCode, T t);

    }

    public interface ReadCallback<T>{

        public void readCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode,T t);

    }

    public interface ReadManyCallback<T>{

        public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<T> t);

    }

    public interface UpdateCallback {

        public void updateCallback(boolean isOffline, boolean isSuccessful,int httpStatusCode);
    }

    public interface DeleteCallback {

        public void deleteShopCallback( boolean isOffline,boolean isSuccessful, int httpStatusCode);
    }

}
