package org.localareadelivery.distributorapp.StandardInterfaces;

import org.localareadelivery.distributorapp.Model.ItemCategory;

import java.util.Map;

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
