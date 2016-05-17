package org.localareadelivery.distributorapp.StandardInterfaces;

import java.util.Map;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DAO<T> {

    public void create(T t,DataSubscriber.CreateCallback<T> createCallback);

    public void update(T t,DataSubscriber.UpdateCallback updateCallback);

    public void delete(int ID, DataSubscriber.DeleteCallback deleteCallback);

    public void read(int ID, DataSubscriber.ReadCallback<T> readCallback);

    public void readMany(Map<String,String> queryParams, DataSubscriber.ReadManyCallback readManyCallback);

}
