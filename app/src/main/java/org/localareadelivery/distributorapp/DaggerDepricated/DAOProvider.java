package org.localareadelivery.distributorapp.DaggerDepricated;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 19/5/16.
 */

@Module
public class DAOProvider {

    //@Singleton

    @Provides
    ShopDAO provideShopDAO()
    {
        return ShopDAO.getInstance();
    }



}
