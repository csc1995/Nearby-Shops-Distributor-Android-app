package org.localareadelivery.distributorapp.DaggerDepricated;

import org.localareadelivery.distributorapp.DataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ShopRESTInterface;
import org.localareadelivery.distributorapp.StandardInterfacesGeneric.DataRouter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 14/5/16.
 */

@Module
public class RestInterfaceModule {


    @Provides
    ShopDAO.ShopRESTContract providesShopRESTInterface()
    {
        return ShopRESTInterface.getInstance();
    }


    @Provides
    DataRouter<ItemCategory> providesItemCategoryDataRouter()
    {
        return new ItemCategoryDataRouter();
    }

}
