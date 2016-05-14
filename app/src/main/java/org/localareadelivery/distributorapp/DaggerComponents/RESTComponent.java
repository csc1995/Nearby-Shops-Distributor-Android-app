package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.DaggerModules.RestInterfaceModule;

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
