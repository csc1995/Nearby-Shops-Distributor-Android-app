package org.localareadelivery.distributorapp.zzdeprecatedCode.Depricated;

import org.localareadelivery.distributorapp.zzStandardInterfacesGeneric.DataSubscriber;

import java.util.Map;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DAO<T> {

    public void create(T t,DataSubscriber<T> createCallback);

    public void update(T t,DataSubscriber updateCallback);

    public void delete(int ID, DataSubscriber deleteCallback);

    public void read(int ID, DataSubscriber<T> readCallback);

    public void readMany(Map<String,String> queryParams, DataSubscriber readManyCallback);

}
