package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.DaggerModules.DAOProvider;
import org.localareadelivery.distributorapp.ShopList.Home;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */


@Singleton
@Component(modules = {DAOProvider.class})
public interface DAOComponent {

    void Inject(Home home);

}
