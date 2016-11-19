package org.localareadelivery.distributorapp.DeliveryGuyAccounts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 17/11/16.
 */


class PagerAdapter extends FragmentPagerAdapter {

    PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a AccountsFragment (defined as a static inner class below).

        if(position==0)
        {
            return AccountsFragment.newInstance(true);
        }
        else
        {
            return AccountsFragment.newInstance(false);
        }

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
                return titleEnabled;
            case 1:
                return titleDisabled;
            case 2:
                return "SECTION 3";
        }
        return null;
    }



    private String titleEnabled = "Enabled (0)";
    private String titleDisabled = "Disabled (0)";
    private String titlePacked = "Packed (0/0)";
    private String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleEnabled = title;
        }
        else if (tabPosition == 1)
        {

            titleDisabled = title;
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
