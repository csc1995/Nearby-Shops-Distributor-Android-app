package org.localareadelivery.distributorapp.OrdersHomeDelivery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sumeet on 13/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentStatePagerAdapter implements PlacedOrdersFragment.NotificationReceiver,ConfirmedOrdersFragment.NotificationReceiver ,PackedOrdersFragment.NotificationReceiver,PendingAcceptFragment.NotificationReceiver{



    PlacedOrdersFragment placedOrdersFragment;
    ConfirmedOrdersFragment confirmedOrdersFragment;
    PackedOrdersFragment packedOrdersFragment;
    PendingAcceptFragment pendingAcceptFragment;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if(position == 0)
        {

            placedOrdersFragment = PlacedOrdersFragment.newInstance();

            placedOrdersFragment.setNotificationReceiver(this);

            return placedOrdersFragment;

        }

        else if(position == 1)

        {
            confirmedOrdersFragment = ConfirmedOrdersFragment.newInstance();

            confirmedOrdersFragment.setNotifyPagerAdapter(this);

            return confirmedOrdersFragment;
        }
        else if(position == 2)
        {
            packedOrdersFragment = PackedOrdersFragment.newInstance();

            packedOrdersFragment.setNotificationReceiver(this);

            return  packedOrdersFragment;

        }
        else if(position == 3)
        {
            pendingAcceptFragment = PendingAcceptFragment.newInstance();

            pendingAcceptFragment.setNotificationReceiver(this);

            return pendingAcceptFragment;
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

                int placedOrdersTotal = 0 ;

                if(placedOrdersFragment!=null)
                {
                    if(placedOrdersFragment.dataset!=null)
                    {
                        placedOrdersTotal = placedOrdersFragment.dataset.size();
                    }
                }

                return "Placed (" + String.valueOf(placedOrdersTotal) + ")";

            case 1:

                int confirmedOrdersTotal = 0 ;

                if(confirmedOrdersFragment!=null)
                {
                    if(confirmedOrdersFragment.dataset!=null)
                    {
                        confirmedOrdersTotal = confirmedOrdersFragment.dataset.size();
                    }
                }

                return "Confirmed (" +  String.valueOf(confirmedOrdersTotal) + ")";


            case 2:


                int packedOrdersTotal = 0 ;

                if(packedOrdersFragment!=null)
                {
                    if(packedOrdersFragment.dataset!=null)
                    {
                        packedOrdersTotal = packedOrdersFragment.dataset.size();
                    }
                }


                return "Packed (" + packedOrdersTotal + ")";



            case 3:


                int pendingAcceptOrdersTotal = 0 ;

                if(pendingAcceptFragment!=null)
                {
                    if(pendingAcceptFragment.dataset!=null)
                    {
                        pendingAcceptOrdersTotal = pendingAcceptFragment.dataset.size();

                    }
                }


                return "Pending Accept (" + pendingAcceptOrdersTotal + ")";

        }
        return null;
    }




    @Override
    public void placedOrdersChanged() {

        if(confirmedOrdersFragment!=null)
        {
            confirmedOrdersFragment.onResume();
        }

        notifyDataSetChanged();
    }

    @Override
    public void notifyConfirmedOrdersChanged() {

        if(packedOrdersFragment!=null)
        {
            packedOrdersFragment.onResume();
        }

        notifyDataSetChanged();
    }



    boolean flag = true;


    @Override
    public void notifyPackedOrdersChanged() {


        if (flag) {

            flag = false;

            if (pendingAcceptFragment != null) {
                pendingAcceptFragment.onResume();
            }

        } else
        {
            flag = true;
        }


        notifyDataSetChanged();

    }

    @Override
    public void notifyPendingAcceptChanged() {

        notifyDataSetChanged();

        if(flag){

            flag = false;


            if(packedOrdersFragment!=null)
            {
                packedOrdersFragment.onResume();
            }


        }else
        {
            flag = true;
        }


    }
}

