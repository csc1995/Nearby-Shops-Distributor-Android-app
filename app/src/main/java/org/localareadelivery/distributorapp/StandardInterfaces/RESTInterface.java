package org.localareadelivery.distributorapp.StandardInterfaces;

import java.util.Map;

/**
 * Created by sumeet on 17/5/16.
 */

public interface RESTInterface<T> {



    public void get(int ID,DataSubscriber.ReadCallback<T> callback);

    public void getMany(

            Map<String,String> stringParams,
            Map<String,Integer> intParams,
            Map<String,Boolean> booleanParams,
            DataSubscriber.ReadManyCallback<T> callback
    );

    public void delete(int ID, DataSubscriber.DeleteCallback callback);

    public void post(T t, DataSubscriber.CreateCallback<T> callback);

    public void put(T t, DataSubscriber.UpdateCallback callback);

}