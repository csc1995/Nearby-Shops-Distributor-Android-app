package org.localareadelivery.distributorapp.DaggerDepricated;

import org.localareadelivery.distributorapp.DataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.Database.FakeDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.StandardInterfaces.DataProviderItemCategory;
import org.localareadelivery.distributorapp.StandardInterfacesGeneric.DataSubscriber;

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
    @Named(DataModule.NETWORK_DATA_PROVIDER)
    DataProviderItemCategory providesNetworkDataProvider()
    {
        return new ItemCategoryRetrofitProvider();
    }

    @Provides
    DataSubscriber<ItemCategory> providesDBDataSubscriber()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    @Named(DataModule.DB_DATA_PROVIDER)
    DataProviderItemCategory providesDBDataProvider()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    ItemCategoryDataRouter providesDataRouter(DataSubscriber<ItemCategory> dbDataSubscriber)
    {
        return new ItemCategoryDataRouter();
    }

}
