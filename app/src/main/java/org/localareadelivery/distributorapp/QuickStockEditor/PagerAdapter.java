package org.localareadelivery.distributorapp.QuickStockEditor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sumeet on 16/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentOutOfStock_ (defined as a static inner class below).

        if(position == 0)
        {

            return FragmentOutOfStock.newInstance(FragmentOutOfStock.MODE_RECENTLY_ADDED);
        }
        else if(position == 1)
        {

            return FragmentOutOfStock.newInstance(FragmentOutOfStock.MODE_LOW_STOCK);

        }else if(position == 2)
        {
            return FragmentOutOfStock.newInstance(FragmentOutOfStock.MODE_OUT_OF_STOCK);
        }
        else if(position == 3)
        {

//            return FragmentPriceNotSet.newInstance();
            return FragmentOutOfStock.newInstance(FragmentOutOfStock.MODE_PRICE_NOT_SET);

        }else if(position == 4)
        {
            return FragmentOutOfStock.newInstance(FragmentOutOfStock.MODE_RECENTLY_UPDATED);

        }


        return  FragmentPriceNotSet.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recently Added";
            case 1:

                return "Low Stock";
            case 2:
                return "Out of Stock";
            case 3:
                return "Price not Set";
            case 4:
                return "Recently Updated";

        }
        return null;
    }
}
