package org.localareadelivery.distributorapp.ShopStaffAccounts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 19/12/16.
 */


public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).


        if(position ==0)
        {
            return FragmentShopStaff.newInstance(true);

        }
        else if(position ==1)
        {
            return FragmentShopStaff.newInstance(false);
        }

        return null;
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


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleEnabled = title;
        }
        else if (tabPosition == 1)
        {
            titleDisabled = title;
        }

        notifyDataSetChanged();
    }




}
