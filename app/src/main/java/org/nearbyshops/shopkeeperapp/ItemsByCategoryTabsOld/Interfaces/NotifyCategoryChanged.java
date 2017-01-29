package org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld.Interfaces;


import org.nearbyshops.shopkeeperapp.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
