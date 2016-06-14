package org.localareadelivery.distributorapp.deprecatedCode.Depricated;

import java.util.List;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DataSubscriberDepricated {

    public interface CreateSubscriber<T> {

        public void createCallback(boolean isOffline,boolean isSuccessful,int httpStatusCode, T t);

    }

    public interface ReadSubscriber<T>{

        public void readCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode,T t);

    }

    public interface ReadManySubscriber<T>{

        public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<T> t);

    }

    public interface UpdateSubscriber {

        public void updateCallback(boolean isOffline, boolean isSuccessful,int httpStatusCode);
    }

    public interface DeleteSubscriber {

        public void deleteShopCallback( boolean isOffline,boolean isSuccessful, int httpStatusCode);
    }

}
