package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.EditItemCategoryOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesAdapterOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesFragmentOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.AddItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.EditItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeAdapter;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeFragmentOld;
import org.localareadelivery.distributorapp.CancelledOrders.CancelledByShop.FragmentCancelledByShop;
import org.localareadelivery.distributorapp.CancelledOrders.ReturnedByDeliveryGuy.FragmentReturnedByDG;
import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.DeliveryAccounts.AccountsFragment;
import org.localareadelivery.distributorapp.DeliveryAccounts.DeliveryGuySelection.AccountsSelectionFragment;
import org.localareadelivery.distributorapp.DeliveryAccounts.EditProfile.EditDeliveryFragment;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingDeliveryApproval.PendingDeliveryApprovalDGD;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingHandover.PendingHandoverFragment;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingReturn.PendingReturnByDG;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingReturnCancelledByShop.PendingReturnCancelledByShop;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.OutForDelivery.OutForDeliveryFragment;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingDeliveryApproval.PendingDeliveryApproval;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingReturn.PendingReturnDGI;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingReturnCancelledByShop.PendingReturnCancelledByShopDGI;
import org.localareadelivery.distributorapp.DistributorLogin;
import org.localareadelivery.distributorapp.EditStock.ItemCategories.ItemCategoriesAdapterEditStock;
import org.localareadelivery.distributorapp.EditStock.ItemCategories.ItemCategoriesFragmentEditStock;
import org.localareadelivery.distributorapp.EditStock.Items.FragmentItemsEditor;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditShop.EditShopFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.ShopAdminHome;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.EditItemCategory;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.ItemCategoriesAdapter;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategories.ItemCategoriesFragment;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.AddItem;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.EditItem;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.ItemAdapterTwo;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Items.ItemRemakeFragment;
import org.localareadelivery.distributorapp.ItemCategoriesTypeSimple.ItemCategoriesFragmentSimple;
import org.localareadelivery.distributorapp.OrderDetail.FragmentOrderDetail;
import org.localareadelivery.distributorapp.ShopList.ShopList;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.AddVehicleSelfActivity;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.EditAddressActivity;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.ConfirmItemsForDelivery;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Confirmed.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Packed.PackedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.PendingAccept.PendingAcceptFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Placed.PlacedOrdersFragment;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentOutOfStock;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentPriceNotSet;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParent;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParentAdapter;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingPayments.PaymentsPendingFragment;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.OutForDelivery.FragmentOutForDelivery;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {



    void Inject(PlacedOrdersFragment placedOrdersFragment);

    void Inject(ConfirmedOrdersFragment confirmedOrdersFragment);

    void Inject(PackedOrdersFragment packedOrdersFragment);

    void Inject(DeliveryVehicleActivity deliveryVehicleActivity);

    void Inject(AddVehicleSelfActivity addVehicleSelfActivity);

    void Inject(EditAddressActivity editAddressActivity);

    void Inject(ConfirmItemsForDelivery confirmItemsForDelivery);

    void Inject(PendingAcceptFragment pendingAcceptOrdersFragment);

    void Inject(org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingHandover.PendingHandoverFragment pendingHandoverFragment);

    void Inject(PendingHandoverFragment pendingHandoverFragment);

    void Inject(FragmentOutForDelivery fragmentOutForDelivery);

    void Inject(PaymentsPendingFragment paymentsPendingFragment);

    void Inject(OutForDeliveryFragment outForDeliveryFragment);

    void Inject(org.localareadelivery.distributorapp.DeliveryGuyInventory.PaymentsPending.PaymentsPendingFragment paymentsPendingFragment);

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

    void Inject(PendingDeliveryApproval pendingDeliveryApproval);

    void Inject(PendingDeliveryApprovalDGD pendingDeliveryApprovalDGD);

    void Inject(PendingReturnByDG pendingReturnByDG);

    void Inject(PendingReturnDGI pendingReturnDGI);

    void Inject(FragmentCancelledByShop fragmentCancelledByShop);

    void Inject(FragmentReturnedByDG fragmentReturnedByDG);

    void Inject(PendingReturnCancelledByShop pendingReturnCancelledByShop);

    void Inject(PendingReturnCancelledByShopDGI pendingReturnCancelledByShopDGI);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(AccountsFragment accountsFragment);

    void Inject(EditDeliveryFragment editDeliveryFragment);

    void Inject(AccountsSelectionFragment accountsSelectionFragment);

    void Inject(EditShopAdminFragment editShopAdminFragment);

    void Inject(EditShopFragment editShopFragment);

    void Inject(ShopAdminHome shopAdminHome);

    void Inject(ItemCategoriesFragmentSimple itemCategoriesFragmentSimple);
}
