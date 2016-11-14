package org.localareadelivery.distributorapp.HomeDeliveryInventory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.localareadelivery.distributorapp.HomeDeliveryInventory.Confirmed.ConfirmedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Packed.PackedOrdersFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.PendingAccept.PendingAcceptFragment;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Placed.PlacedOrdersFragment;

/**
 * Created by sumeet on 13/6/16.
 */


public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if(position == 0)
        {
            return PlacedOrdersFragment.newInstance();
        }
        else if(position == 1)
        {
            return ConfirmedOrdersFragment.newInstance();
        }
        else if(position == 2)
        {
            return  PackedOrdersFragment.newInstance();
        }
        else if(position == 3)
        {
            return PendingAcceptFragment.newInstance();
        }


        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:


                return titlePlaced;

            case 1:


                return titleConfirmed;

            case 2:

                return titlePacked;

            case 3:

                return "Pending Accept";

        }
        return null;
    }








    private String titlePlaced = "Placed ( 0 / 0 )";
    private String titleConfirmed = "Confirmed ( 0 / 0 )";
    private String titlePacked = "Packed (0/0)";
    private String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titlePlaced = title;
        }
        else if (tabPosition == 1)
        {

            titleConfirmed = title;
        }else if(tabPosition == 2)
        {
            titlePacked = title;

        }else if(tabPosition == 3)
        {
            titleDetachedItems = title;
        }


        notifyDataSetChanged();
    }




}

