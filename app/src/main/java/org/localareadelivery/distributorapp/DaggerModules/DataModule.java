package org.localareadelivery.distributorapp.DaggerModules;

import org.localareadelivery.distributorapp.DataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.Database.FakeDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.RetrofitDataProviders.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.StandardInterfaces.DataProviderItemCategory;
import org.localareadelivery.distributorapp.StandardInterfaces.DataSubscriber;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 19/5/16.
 */

@Module
public class DataModule {

    final static public String NETWORK_DATA_PROVIDER = "NetworkDataProvider";
    final static public String DB_DATA_PROVIDER = "DBDataProvider";

    public DataModule() {
    }

    @Provides
    @Singleton
    @Named(DataModule.NETWORK_DATA_PROVIDER)
    DataProviderItemCategory providesNetworkDataProvider()
    {
        return new ItemCategoryRetrofitProvider();
    }

    @Provides
    @Singleton
    DataSubscriber<ItemCategory> providesDBDataSubscriber()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    @Singleton
    @Named(DataModule.DB_DATA_PROVIDER)
    DataProviderItemCategory providesDBDataProvider()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    @Singleton
    ItemCategoryDataRouter providesDataRouter(DataSubscriber<ItemCategory> dbDataSubscriber)
    {
        return new ItemCategoryDataRouter();
    }

}
