package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.EditItemCategoryOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesAdapterOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesFragmentOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.AddItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.EditItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeAdapter;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeFragmentOld;
import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.DistributorLogin;
import org.localareadelivery.distributorapp.EditStock.ItemCategories.ItemCategoriesAdapterEditStock;
import org.localareadelivery.distributorapp.EditStock.ItemCategories.ItemCategoriesFragmentEditStock;
import org.localareadelivery.distributorapp.EditStock.Items.FragmentItemsEditor;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.EditItemCategory;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.ItemCategoriesAdapter;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.ItemCategoriesFragment;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.AddItem;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.EditItem;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.ItemAdapterTwo;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.ItemRemakeFragment;
import org.localareadelivery.distributorapp.ShopList.ShopList;
import org.localareadelivery.distributorapp.zzDataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.localareadelivery.distributorapp.zzDataProvidersRetrofit.ShopRESTInterface;
import org.localareadelivery.distributorapp.DeliveryGuy.AddVehicleSelfActivity;
import org.localareadelivery.distributorapp.DeliveryGuy.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.DeliveryGuy.EditAddressActivity;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.ConfirmItemsForDelivery;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PackedOrdersFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PendingAcceptFragment;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.PlacedOrdersFragment;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentOutOfStock;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentPriceNotSet;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParent;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParentAdapter;
import org.localareadelivery.distributorapp.VehicleDriverDashboard.PaymentsPendingFragment;
import org.localareadelivery.distributorapp.VehicleDriverDashboard.PendingHandoverFragment;

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

    void Inject(ShopList shopList);

    void Inject(AddItemOld addItemOld);

    void Inject(ItemRemakeAdapter itemRemakeAdapter);

    void Inject(EditItemCategoryOld editItemCategoryOld);

    void Inject(ItemCategoriesAdapterOld itemCategoriesAdapterOld);

    void Inject(ItemCategoriesFragmentOld itemCategoriesFragmentOld);

    void Inject(EditItemOld editItemOld);

    void Inject(ItemCategoriesParent itemCategoriesParent);


    void Inject(ItemCategoriesParentAdapter itemCategoriesParentAdapter);

    void Inject(DistributorLogin distributorLogin);

    void Inject(ItemRemakeFragment itemRemakeFragment);

    void Inject(ItemAdapterTwo itemAdapterTwo);

    void Inject(EditItem editItem);

    void Inject(AddItem addItem);

    void Inject(EditItemCategory editItemCategory);

    void Inject(ItemCategoriesAdapter itemCategoriesAdapter);

    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(ItemRemakeFragmentOld itemRemakeFragmentOld);

    void Inject(ItemCategoriesAdapterEditStock itemCategoriesAdapterEditStock);

    void Inject(ItemCategoriesFragmentEditStock itemCategoriesFragmentEditStock);

    void Inject(FragmentItemsEditor fragmentItemsEditor);
}
