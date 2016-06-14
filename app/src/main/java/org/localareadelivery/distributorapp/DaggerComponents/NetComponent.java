package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ShopRESTInterface;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.AddVehicleSelfActivity;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.EditAddressActivity;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.AdapterPlacedOrders;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PackedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PlacedOrdersFragment;

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

    void inject(ItemCategoryRetrofitProvider provider);

    void Inject(PlacedOrdersFragment placedOrdersFragment);

    void Inject(ConfirmedOrdersFragment confirmedOrdersFragment);

    void Inject(PackedOrdersFragment packedOrdersFragment);

    void Inject(DeliveryVehicleActivity deliveryVehicleActivity);

    void Inject(AddVehicleSelfActivity addVehicleSelfActivity);

    void Inject(EditAddressActivity editAddressActivity);

}
