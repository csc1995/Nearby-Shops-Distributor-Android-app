package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.RetrofitRESTInterfaces.ShopRESTInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(ShopRESTInterface shopRESTInterface);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}
