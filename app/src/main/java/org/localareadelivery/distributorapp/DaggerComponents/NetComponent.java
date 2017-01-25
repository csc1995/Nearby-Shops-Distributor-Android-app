package org.localareadelivery.distributorapp.DaggerComponents;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.EditItemCategoryOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesAdapterOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategories.ItemCategoriesFragmentOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.AddItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.EditItemOld;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeAdapter;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.Items.ItemRemakeFragmentOld;
import org.localareadelivery.distributorapp.CancelledOrders.CancelledByEndUser.FragmentCancelledByUser;
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
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.PendingReturnCancelledByUser.PendingReturnCancelledByUser;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.OutForDelivery.OutForDeliveryFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PendingDeliveryApproval.PendingDeliveryApproval;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PendingReturn.PendingReturnDGI;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PendingReturnCancelledByShop.PendingReturnCancelledByShopDGI;
import org.localareadelivery.distributorapp.DistributorLogin;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PendingReturnCancelledByUser.PendingReturnCancelledByUserDGI;
import org.localareadelivery.distributorapp.OrderDetailPFS.FragmentOrderDetailPFS;
import org.localareadelivery.distributorapp.OrderHistoryHD.Complete.CompleteOrdersFragment;
import org.localareadelivery.distributorapp.OrderHistoryHD.Pending.PendingOrdersFragment;
import org.localareadelivery.distributorapp.Items.ItemsFragmentSimple;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.ItemsByCatFragmentSimple;
import org.localareadelivery.distributorapp.ItemsInShop.ItemsInStockFragment;
import org.localareadelivery.distributorapp.ItemsInShopOld.ItemCategories.ItemCategoriesAdapterEditStock;
import org.localareadelivery.distributorapp.ItemsInShopOld.ItemCategories.ItemCategoriesFragmentEditStock;
import org.localareadelivery.distributorapp.ItemsInShopOld.Items.FragmentItemsEditor;
import org.localareadelivery.distributorapp.ItemsInShopByCat.ItemsInStockByCatFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditShop.EditShopFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.ShopAdminHome;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.ItemCategories.EditItemCategory;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.ItemCategories.ItemCategoriesAdapter;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.ItemCategories.ItemCategoriesFragment;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.Items.AddItem;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.Items.EditItem;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.Items.ItemAdapterTwo;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.Items.ItemRemakeFragment;
import org.localareadelivery.distributorapp.OrderDetail.FragmentOrderDetail;
import org.localareadelivery.distributorapp.OrderHistoryPFS.Complete.CompleteOrdersFragmentPFS;
import org.localareadelivery.distributorapp.OrderHistoryPFS.Pending.PendingOrdersFragmentPFS;
import org.localareadelivery.distributorapp.PickFromShopInventory.Confirmed.ConfirmedOrdersFragmentPFS;
import org.localareadelivery.distributorapp.PickFromShopInventory.Packed.PackedOrdersFragmentPFS;
import org.localareadelivery.distributorapp.PickFromShopInventory.Placed.PlacedOrdersFragmentPFS;
import org.localareadelivery.distributorapp.ShopStaffAccounts.EditShopStaff.EditStaffFragment;
import org.localareadelivery.distributorapp.ShopStaffAccounts.FragmentShopStaff;
import org.localareadelivery.distributorapp.ShopStaffHome.EditStaffSelf.EditStaffSelfFragment;
import org.localareadelivery.distributorapp.ShopStaffHome.ShopStaffHome;
import org.localareadelivery.distributorapp.zDeprecatedCode.ShopList.ShopList;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.AddVehicleSelfActivity;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy.EditAddressActivity;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.ConfirmItemsForDelivery;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Confirmed.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Packed.PackedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.PendingAccept.PendingAcceptFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Placed.PlacedOrdersFragment;
import org.localareadelivery.distributorapp.QuickStockEditor.FragmentOutOfStock;
import org.localareadelivery.distributorapp.QuickStockEditor.Unused.FragmentPriceNotSet;
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

    void Inject(org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PendingHandover.PendingHandoverFragment pendingHandoverFragment);

    void Inject(PendingHandoverFragment pendingHandoverFragment);

    void Inject(FragmentOutForDelivery fragmentOutForDelivery);

    void Inject(PaymentsPendingFragment paymentsPendingFragment);

    void Inject(OutForDeliveryFragment outForDeliveryFragment);

    void Inject(org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.PaymentsPending.PaymentsPendingFragment paymentsPendingFragment);

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

    void Inject(ItemsByCatFragmentSimple itemsByCatFragmentSimple);

    void Inject(ItemsInStockByCatFragment itemsInStockByCatFragment);

    void Inject(ItemsInStockFragment itemsInStockFragment);

    void Inject(ItemsFragmentSimple itemsFragmentSimple);

    void Inject(FragmentShopStaff fragmentShopStaff);

    void Inject(EditStaffFragment editStaffFragment);

    void Inject(ShopStaffHome shopStaffHome);

    void Inject(EditStaffSelfFragment editStaffSelfFragment);

    void Inject(PendingOrdersFragment pendingOrdersFragment);

    void Inject(CompleteOrdersFragment completeOrdersFragment);

    void Inject(FragmentCancelledByUser fragmentCancelledByUser);

    void Inject(PendingReturnCancelledByUserDGI pendingReturnCancelledByUserDGI);

    void Inject(PendingReturnCancelledByUser pendingReturnCancelledByUser);

    void Inject(CompleteOrdersFragmentPFS completeOrdersFragmentPFS);

    void Inject(FragmentOrderDetailPFS fragmentOrderDetailPFS);

    void Inject(PendingOrdersFragmentPFS pendingOrdersFragmentPFS);

    void Inject(PlacedOrdersFragmentPFS placedOrdersFragmentPFS);

    void Inject(ConfirmedOrdersFragmentPFS confirmedOrdersFragmentPFS);

    void Inject(PackedOrdersFragmentPFS packedOrdersFragmentPFS);
}
