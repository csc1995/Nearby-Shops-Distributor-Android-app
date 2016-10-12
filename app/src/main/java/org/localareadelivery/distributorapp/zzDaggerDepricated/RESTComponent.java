package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.zzDAOs.ShopDAO;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules = {RestInterfaceModule.class})
public interface RESTComponent {

    void inject(ShopDAO shopDAO);
}
