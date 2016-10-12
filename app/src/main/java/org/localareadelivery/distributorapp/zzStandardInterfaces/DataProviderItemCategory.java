package org.localareadelivery.distributorapp.zzStandardInterfaces;

import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.zzStandardInterfacesGeneric.DataProvider;
import org.localareadelivery.distributorapp.zzStandardInterfacesGeneric.DataSubscriber;

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
