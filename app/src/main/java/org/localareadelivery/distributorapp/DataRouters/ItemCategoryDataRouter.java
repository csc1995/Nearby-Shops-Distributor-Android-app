package org.localareadelivery.distributorapp.DataRouters;

import android.provider.ContactsContract;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.DaggerModules.DataModule;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.StandardInterfaces.DataProvider;
import org.localareadelivery.distributorapp.StandardInterfaces.DataProviderItemCategory;
import org.localareadelivery.distributorapp.StandardInterfaces.DataRouter;
import org.localareadelivery.distributorapp.StandardInterfaces.DataSubscriber;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by sumeet on 19/5/16.
 */
public class ItemCategoryDataRouter implements DataRouter<ItemCategory>{


    @Inject @Named(DataModule.NETWORK_DATA_PROVIDER)
    DataProviderItemCategory networkDataProvider;

    @Inject @Named(DataModule.DB_DATA_PROVIDER)
    DataProviderItemCategory dbDataProvider;

    @Inject
    DataSubscriber<ItemCategory> dbDataSubscriber;


    public ItemCategoryDataRouter() {

        DaggerComponentBuilder.getInstance()
                .getDataComponent()
                .Inject(this);

        // save the recieved data into the database whenever the network call is made
        networkDataProvider.subscribe(dbDataSubscriber);
    }


    public ItemCategoryDataRouter(DataProviderItemCategory networkDataProvider,
                                  DataProviderItemCategory dbDataProvider,
                                  DataSubscriber<ItemCategory> dbDataSubscriber)

    {
        this.networkDataProvider = networkDataProvider;
        this.dbDataProvider = dbDataProvider;
        this.dbDataSubscriber = dbDataSubscriber;

        networkDataProvider.subscribe(dbDataSubscriber);
    }



    @Override
    public DataProviderItemCategory getDataProvider() {

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // network is available . Make a network call using network data router
            return networkDataProvider;
        }

        else
        {
            // network is not available.
            return dbDataProvider;
        }
    }

}