package org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces;


import org.localareadelivery.distributorapp.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
