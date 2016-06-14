package org.localareadelivery.distributorapp.DaggerDepricated;

import org.localareadelivery.distributorapp.DaggerDepricated.DAOProvider;
import org.localareadelivery.distributorapp.ShopList.AddShop;
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

    void Inject(AddShop addShop);

}
