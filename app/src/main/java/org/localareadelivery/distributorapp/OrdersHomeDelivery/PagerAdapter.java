package org.localareadelivery.distributorapp.OrdersHomeDelivery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 13/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentPagerAdapter {



    PlacedOrdersFragment placedOrdersFragment;
    ConfirmedOrdersFragment confirmedOrdersFragment;
    PackedOrdersFragment packedOrdersFragment;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if(position == 0)
        {

            placedOrdersFragment = PlacedOrdersFragment.newInstance();

            return placedOrdersFragment;

        }

        else if(position == 1)

        {
            confirmedOrdersFragment = ConfirmedOrdersFragment.newInstance();

            return confirmedOrdersFragment;
        }
        else if(position == 2)
        {
            packedOrdersFragment = PackedOrdersFragment.newInstance();

            return  packedOrdersFragment;
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
                return "Placed (5)";
            case 1:
                return "Confirmed (2)";
            case 2:
                return "Packed (4)";
        }
        return null;
    }
}
