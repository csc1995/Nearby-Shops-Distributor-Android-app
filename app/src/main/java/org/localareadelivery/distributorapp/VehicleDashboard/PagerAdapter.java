package org.localareadelivery.distributorapp.VehicleDashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.localareadelivery.distributorapp.ModelStats.DeliveryVehicleSelf;

/**
 * Created by sumeet on 15/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    DeliveryVehicleSelf deliveryVehicle;

    int vehicleID;


    PendingAcceptFragment pendingAcceptFragment;


    public PagerAdapter(FragmentManager fm, DeliveryVehicleSelf deliveryVehicleSelf) {
        super(fm);

        deliveryVehicle = deliveryVehicleSelf;

        if(deliveryVehicle!=null)
        {
            vehicleID = deliveryVehicle.getID();
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).



        if(position==0)
        {
            pendingAcceptFragment = PendingAcceptFragment.newInstance();

            pendingAcceptFragment.setDeliveryVehicleSelf(deliveryVehicle);

            return pendingAcceptFragment;
        }


        return PlaceholderFragment.newInstance(vehicleID);
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
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}