package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.zzDAOs.ShopDAO;

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
