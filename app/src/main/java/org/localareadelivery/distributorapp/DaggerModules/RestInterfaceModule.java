package org.localareadelivery.distributorapp.DaggerModules;

import org.localareadelivery.distributorapp.DataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.RetrofitDataProviders.ShopRESTInterface;
import org.localareadelivery.distributorapp.StandardInterfaces.DataRouter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 14/5/16.
 */

@Module
public class RestInterfaceModule {


    @Provides
    @Singleton
    ShopDAO.ShopRESTContract providesShopRESTInterface()
    {
        return ShopRESTInterface.getInstance();
    }


    @Provides
    @Singleton
    DataRouter<ItemCategory> providesItemCategoryDataRouter()
    {
        return new ItemCategoryDataRouter();
    }

}
