package org.localareadelivery.distributorapp.DaggerModules;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.RetrofitRESTCalls.ShopCalls;

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

        return ShopCalls.getInstance();
    }


}
