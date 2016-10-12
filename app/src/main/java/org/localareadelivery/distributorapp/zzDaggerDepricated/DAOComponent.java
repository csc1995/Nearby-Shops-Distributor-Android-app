package org.localareadelivery.distributorapp.zzDaggerDepricated;

import org.localareadelivery.distributorapp.ShopList.AddShop;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */


@Singleton
@Component(modules = {DAOProvider.class})
public interface DAOComponent {

    void Inject(AddShop addShop);

}
