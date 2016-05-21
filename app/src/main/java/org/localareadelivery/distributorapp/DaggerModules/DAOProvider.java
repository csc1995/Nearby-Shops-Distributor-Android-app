package org.localareadelivery.distributorapp.DaggerModules;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.StandardInterfaces.Depricated.DAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 19/5/16.
 */

@Module
public class DAOProvider {

    @Provides
    @Singleton
    ShopDAO provideShopDAO()
    {
        return ShopDAO.getInstance();
    }



}
