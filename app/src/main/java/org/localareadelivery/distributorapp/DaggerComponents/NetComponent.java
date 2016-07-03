package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesAdapter;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesFragment;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeAdapter;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeFragment;
import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.DataProvidersRetrofit.ShopRESTInterface;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.AddVehicleSelfActivity;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.EditAddressActivity;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.ConfirmItemsForDelivery;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PackedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PendingAcceptFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PlacedOrdersFragment;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentOutOfStock;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentPriceNotSet;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParent;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParentAdapter;
import org.localareadelivery.distributorapp.ShopList.Home;
import org.localareadelivery.distributorapp.VehicleDriverDashboard.PaymentsPendingFragment;
import org.localareadelivery.distributorapp.VehicleDriverDashboard.PendingHandoverFragment;
import org.localareadelivery.distributorapp.DeprecatedAddItems.ItemCategories.EditItemCategory;
import org.localareadelivery.distributorapp.DeprecatedAddItems.ItemCategories.ItemCategories;
import org.localareadelivery.distributorapp.DeprecatedAddItems.Items.AddItem;
import org.localareadelivery.distributorapp.DeprecatedAddItems.Items.EditItem;
import org.localareadelivery.distributorapp.DeprecatedAddItems.Items.ItemsAdapter;

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

    void Inject(ConfirmItemsForDelivery confirmItemsForDelivery);

    void Inject(PendingAcceptFragment pendingAcceptOrdersFragment);

    void Inject(org.localareadelivery.distributorapp.VehicleInventory.PendingAcceptFragment pendingAcceptFragment);

    void Inject(org.localareadelivery.distributorapp.VehicleDriverDashboard.PendingAcceptFragment pendingAcceptFragment);

    void Inject(PendingHandoverFragment pendingHandoverFragment);

    void Inject(PaymentsPendingFragment paymentsPendingFragment);

    void Inject(org.localareadelivery.distributorapp.VehicleInventory.PendingHandoverFragment pendingHandoverFragment);

    void Inject(org.localareadelivery.distributorapp.VehicleInventory.PaymentsPendingFragment paymentsPendingFragment);

    void Inject(FragmentOutOfStock outOfStockFragment);

    void Inject(FragmentPriceNotSet fragmentPriceNotSet);


    void Inject(EditItemCategory editItemCategory);

    void Inject(EditItem editItem);

    void Inject(ItemCategories itemCategories);

    void Inject(ItemsAdapter itemsAdapter);

    void Inject(Home home);

    void Inject(AddItem addItem);

    void Inject(org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.AddItem addItem);

    void Inject(ItemRemakeAdapter itemRemakeAdapter);

    void Inject(org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.EditItemCategory editItemCategory);

    void Inject(ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.EditItem editItem);

    void Inject(ItemRemakeFragment itemRemakeFragment);

    void Inject(ItemCategoriesParent itemCategoriesParent);

    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);
}
