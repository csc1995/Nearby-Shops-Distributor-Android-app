package org.localareadelivery.distributorapp.AddItemsToShopInventory;

import org.localareadelivery.distributorapp.Model.ItemCategory;

/**
 * Created by sumeet on 27/6/16.
 */
public interface FragmentsNotificationReceiver {


        void itemCategoryChanged(ItemCategory currentCategory);

        void showAppBar();

        void hideAppBar();


        void insertTab(String categoryName);

        void removeLastTab();

        void notifySwipeToright();
}
