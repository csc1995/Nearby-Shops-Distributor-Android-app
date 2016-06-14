package org.localareadelivery.distributorapp.DaggerDepricated;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.DaggerDepricated.RestInterfaceModule;

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
