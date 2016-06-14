package org.localareadelivery.distributorapp.StandardInterfaces;

import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.StandardInterfacesGeneric.DataProvider;
import org.localareadelivery.distributorapp.StandardInterfacesGeneric.DataSubscriber;

/**
 * Created by sumeet on 19/5/16.
 */
public interface DataProviderItemCategory extends DataProvider<ItemCategory> {


    public void readMany(
            int parentID,
            int shopID,
            DataSubscriber<ItemCategory> Subscriber
    );

}
