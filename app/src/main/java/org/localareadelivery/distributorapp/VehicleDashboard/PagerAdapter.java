package org.localareadelivery.distributorapp.VehicleDashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.localareadelivery.distributorapp.ModelStats.DeliveryVehicleSelf;
import org.localareadelivery.distributorapp.VehicleDriverDashboard.*;

/**
 * Created by sumeet on 15/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    DeliveryVehicleSelf deliveryVehicle;

    int vehicleID;


    PendingAcceptFragment pendingAcceptFragment;
    PendingHandoverFragment pendingHandOverFragment;
    PaymentsPendingFragment paymentsPendingFragment;


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
        else if(position == 1)
        {
            pendingHandOverFragment = PendingHandoverFragment.newInstance();

            pendingHandOverFragment.setDeliveryVehicleSelf(deliveryVehicle);

            return  pendingHandOverFragment;

        }else if (position == 2)
        {
            paymentsPendingFragment = PaymentsPendingFragment.newInstance();

            paymentsPendingFragment.setDeliveryVehicleSelf(deliveryVehicle);

            return paymentsPendingFragment;
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
                return "Pending Accept";
            case 1:
                return "Pending Handover";
            case 2:
                return "Payments Pending";
            case 3:
                return "User approval Pending";

        }
        return null;
    }
}