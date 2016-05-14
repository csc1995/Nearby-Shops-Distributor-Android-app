package org.localareadelivery.distributorapp.DaggerModules;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 14/5/16.
 */
@Module
public class DAOProvider {


    @Provides
    @Singleton
    ShopDAO providesShopDAO()
    {
        return ShopDAO.getInstance();
    }

}
