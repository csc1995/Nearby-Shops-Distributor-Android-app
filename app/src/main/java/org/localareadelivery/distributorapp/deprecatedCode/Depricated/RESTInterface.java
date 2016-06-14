package org.localareadelivery.distributorapp.deprecatedCode.Depricated;

import org.localareadelivery.distributorapp.StandardInterfacesGeneric.DataSubscriber;

import java.util.Map;

/**
 * Created by sumeet on 17/5/16.
 */

public interface RESTInterface<T> {



    public void get(int ID,DataSubscriber<T> callback);

    public void getMany(

            Map<String,String> stringParams,
            Map<String,Integer> intParams,
            Map<String,Boolean> booleanParams,
            DataSubscriber<T> callback
    );

    public void delete(int ID, DataSubscriber callback);

    public void post(T t, DataSubscriber<T> callback);

    public void put(T t, DataSubscriber callback);

}