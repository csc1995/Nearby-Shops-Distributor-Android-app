package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.zzDataRouters.ItemCategoryDataRouter;
import org.localareadelivery.distributorapp.zzDAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.zzDataProvidersRetrofit.ShopRESTInterface;
import org.localareadelivery.distributorapp.zzStandardInterfacesGeneric.DataRouter;

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
