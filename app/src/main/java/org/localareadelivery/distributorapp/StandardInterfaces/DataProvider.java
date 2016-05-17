package org.localareadelivery.distributorapp.StandardInterfaces;

import java.util.Map;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DataProvider<T> {

    // IRUD operations - Insert, Read, Update, Delete


    public void read(
            int ID,
            DataSubscriber.ReadCallback<T> callback
    );


    public void readMany(

            Map<String,String> stringParams,
            Map<String,Integer> intParams,
            Map<String,Boolean> booleanParams,
            DataSubscriber.ReadManyCallback<T> callback
    );

    public void delete(
            int ID,
            DataSubscriber.DeleteCallback callback
    );

    public void insert(
            T t,
            DataSubscriber.CreateCallback<T> callback
    );

    public void update(
            T t,
            DataSubscriber.UpdateCallback callback
    );



    public void subscribeRead(DataSubscriber.ReadCallback<T> callback);

    public void subscribeReadMany(DataSubscriber.ReadManyCallback<T> callback);

    public void subscribeDelete(DataSubscriber.DeleteCallback callback);

    public void subscribeInsert(DataSubscriber.CreateCallback<T> callback);

    public void subscribeUpdate(DataSubscriber.UpdateCallback callback);

}
