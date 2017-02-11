package org.nearbyshops.shopkeeperapp.DaggerComponents;

import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.ItemCategories.EditItemCategoryOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.ItemCategories.ItemCategoriesAdapterOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.ItemCategories.ItemCategoriesFragmentOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.Items.AddItemOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.Items.EditItemOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.Items.ItemRemakeAdapter;

import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.Items.ItemRemakeFragmentOld;
import org.nearbyshops.shopkeeperapp.CancelledOrdersHD.CancelledByEndUser.FragmentCancelledByUser;
import org.nearbyshops.shopkeeperapp.CancelledOrdersHD.CancelledByShop.FragmentCancelledByShop;
import org.nearbyshops.shopkeeperapp.CancelledOrdersHD.ReturnedByDeliveryGuy.FragmentReturnedByDG;
import org.nearbyshops.shopkeeperapp.DaggerModules.AppModule;
import org.nearbyshops.shopkeeperapp.DaggerModules.NetModule;
import org.nearbyshops.shopkeeperapp.DeliveryAccounts.AccountsFragment;
import org.nearbyshops.shopkeeperapp.DeliveryAccounts.DeliveryGuySelection.AccountsSelectionFragment;
import org.nearbyshops.shopkeeperapp.DeliveryAccounts.EditProfile.EditDeliveryFragment;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingDeliveryApproval.PendingDeliveryApprovalDGD;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingHandover.PendingHandoverFragment;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturn.PendingReturnByDG;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturnCancelledByShop.PendingReturnCancelledByShop;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturnCancelledByUser.PendingReturnCancelledByUser;
import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.EditProfile.EditDeliverySelfFragment;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.OutForDelivery.OutForDeliveryFragment;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingDeliveryApproval.PendingDeliveryApproval;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingReturn.PendingReturnDGI;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingReturnCancelledByShop.PendingReturnCancelledByShopDGI;
import org.nearbyshops.shopkeeperapp.DistributorLogin;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingReturnCancelledByUser.PendingReturnCancelledByUserDGI;
import org.nearbyshops.shopkeeperapp.OrderDetailPFS.FragmentOrderDetailPFS;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.Complete.CompleteOrdersFragment;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.Pending.PendingOrdersFragment;
import org.nearbyshops.shopkeeperapp.Items.ItemsFragmentSimple;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.ItemsByCatFragmentSimple;
import org.nearbyshops.shopkeeperapp.ItemsInShop.ItemsInShopFragment;
import org.nearbyshops.shopkeeperapp.ItemsInShopOld.ItemCategories.ItemCategoriesAdapterEditStock;
import org.nearbyshops.shopkeeperapp.ItemsInShopOld.ItemCategories.ItemCategoriesFragmentEditStock;
import org.nearbyshops.shopkeeperapp.ItemsInShopOld.Items.FragmentItemsEditor;
import org.nearbyshops.shopkeeperapp.ItemsInShopByCat.ItemsInStockByCatFragment;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.EditProfile.EditShopAdminFragment;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.EditShop.EditShopFragment;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.ShopAdminHome;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.ItemCategories.EditItemCategory;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.ItemCategories.ItemCategoriesAdapter;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.Items.AddItem;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.Items.EditItem;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.Items.ItemAdapterTwo;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.Items.ItemRemakeFragment;
import org.nearbyshops.shopkeeperapp.OrderDetailHD.FragmentOrderDetail;
import org.nearbyshops.shopkeeperapp.OrderHistoryPFS.Complete.CompleteOrdersFragmentPFS;
import org.nearbyshops.shopkeeperapp.OrderHistoryPFS.Pending.PendingOrdersFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopCancelled.CancelledByShop.CancelledByShopFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopCancelled.CancelledByUser.CancelledByUserFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopInventory.Confirmed.ConfirmedOrdersFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopInventory.Packed.PackedOrdersFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopInventory.PendingDelivery.PendingDeliveryFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopInventory.PendingPayments.PaymentsPendingFragmentPFS;
import org.nearbyshops.shopkeeperapp.PickFromShopInventory.Placed.PlacedOrdersFragmentPFS;
import org.nearbyshops.shopkeeperapp.Services.ServiceFragment.ServicesFragment;
import org.nearbyshops.shopkeeperapp.Services.SubmitURLDialog.SubmitURLDialog;
import org.nearbyshops.shopkeeperapp.ShopStaffAccounts.EditShopStaff.EditStaffFragment;
import org.nearbyshops.shopkeeperapp.ShopStaffAccounts.FragmentShopStaff;
import org.nearbyshops.shopkeeperapp.ShopStaffHome.EditStaffSelf.EditStaffSelfFragment;
import org.nearbyshops.shopkeeperapp.ShopStaffHome.ShopStaffHome;
import org.nearbyshops.shopkeeperapp.zDeprecatedCode.ShopList.ShopList;
import org.nearbyshops.shopkeeperapp.aaDeprecated_DeliveryGuy.AddVehicleSelfActivity;
import org.nearbyshops.shopkeeperapp.aaDeprecated_DeliveryGuy.DeliveryVehicleActivity;
import org.nearbyshops.shopkeeperapp.aaDeprecated_DeliveryGuy.EditAddressActivity;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.ConfirmItemsForDelivery;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.Confirmed.ConfirmedOrdersFragment;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.Packed.PackedOrdersFragment;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.PendingAccept.PendingAcceptFragment;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.Placed.PlacedOrdersFragment;
import org.nearbyshops.shopkeeperapp.QuickStockEditor.FragmentOutOfStock;
import org.nearbyshops.shopkeeperapp.QuickStockEditor.Unused.FragmentPriceNotSet;
import org.nearbyshops.shopkeeperapp.SelectParent.ItemCategoriesParent;
import org.nearbyshops.shopkeeperapp.SelectParent.ItemCategoriesParentAdapter;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingPayments.PaymentsPendingFragment;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.OutForDelivery.FragmentOutForDelivery;

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

    void Inject(org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingHandover.PendingHandoverFragment pendingHandoverFragment);

    void Inject(PendingHandoverFragment pendingHandoverFragment);

    void Inject(FragmentOutForDelivery fragmentOutForDelivery);

    void Inject(PaymentsPendingFragment paymentsPendingFragment);

    void Inject(OutForDeliveryFragment outForDeliveryFragment);

    void Inject(org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PaymentsPending.PaymentsPendingFragment paymentsPendingFragment);

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

    void Inject(ItemsInShopFragment itemsInShopFragment);

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

    void Inject(PaymentsPendingFragmentPFS paymentsPendingFragmentPFS);

    void Inject(PendingDeliveryFragmentPFS pendingDeliveryFragmentPFS);

    void Inject(CancelledByShopFragmentPFS cancelledByShopFragmentPFS);

    void Inject(CancelledByUserFragmentPFS cancelledByUserFragmentPFS);

    void Inject(EditDeliverySelfFragment editDeliverySelfFragment);

    void Inject(SubmitURLDialog submitURLDialog);

    void Inject(ServicesFragment servicesFragment);
}
