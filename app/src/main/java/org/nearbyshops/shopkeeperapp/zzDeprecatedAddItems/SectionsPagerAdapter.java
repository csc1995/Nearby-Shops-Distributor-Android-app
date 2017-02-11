package org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.ItemCategories.ItemCategoriesFragmentOld;
import org.nearbyshops.shopkeeperapp.zzDeprecatedAddItems.Items.ItemRemakeFragmentOld;

/**
 * Created by sumeet on 27/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

//    DetachedTabs activity;

    public SectionsPagerAdapter(FragmentManager fm, ItemCategoriesTabs activity) {
        super(fm);

//        this.activity = activity;
    }


    ItemCategoriesFragmentOld itemCategoriesFragmentOld;

    ItemRemakeFragmentOld itemRemakeFragmentOld;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a AccountsSelectionFragment (defined as a static inner class below).



        if(position == 0)
        {
            itemCategoriesFragmentOld = new ItemCategoriesFragmentOld();

//            activity.setNotificationReceiver(itemCategoriesFragmentOld);

            return itemCategoriesFragmentOld;
        }
        else if (position == 1)
        {

            itemRemakeFragmentOld = new ItemRemakeFragmentOld();

            return itemRemakeFragmentOld;
        }

        return  null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleCategories;
            case 1:
                return titleItems;
            case 2:
                return titleDetachedItemCategories;
            case 3:
                return titleDetachedItems;
        }
        return null;
    }



    String titleCategories = "Sub-Categories (0)";
    String titleItems = "Items (0)";
    String titleDetachedItemCategories = "Detached Item-Categories (0/0)";
    String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleCategories = title;
        }
        else if (tabPosition == 1)
        {

            titleItems = title;
        }else if(tabPosition == 2)
        {
            titleDetachedItemCategories = title;

        }else if(tabPosition == 3)
        {
            titleDetachedItems = title;
        }


        notifyDataSetChanged();
    }




}