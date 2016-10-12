package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.zzDataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.zzDatabase.FakeDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.zzDataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.zzStandardInterfaces.DataProviderItemCategory;
import org.localareadelivery.distributorapp.zzStandardInterfacesGeneric.DataSubscriber;

import javax.inject.Named;

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
