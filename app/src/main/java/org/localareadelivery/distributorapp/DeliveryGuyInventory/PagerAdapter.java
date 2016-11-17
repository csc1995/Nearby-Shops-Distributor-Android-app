package org.localareadelivery.distributorapp.DeliveryGuyInventory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.localareadelivery.distributorapp.DeliveryGuyInventory.OutForDelivery.OutForDeliveryFragment;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PaymentsPending.PaymentsPendingFragment;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingDeliveryApproval.PendingDeliveryApproval;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingHandover.PendingHandoverFragment;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingReturn.PendingReturnDGI;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.PendingReturnCancelledByShop.PendingReturnCancelledByShopDGI;
import org.localareadelivery.distributorapp.Model.DeliveryGuySelf;

/**
 * Created by sumeet on 15/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private DeliveryGuySelf deliveryVehicle;
    private int vehicleID;



    public PagerAdapter(FragmentManager fm, DeliveryGuySelf deliveryGuySelf) {
        super(fm);

        deliveryVehicle = deliveryGuySelf;

        if(deliveryVehicle!=null)
        {
            vehicleID = deliveryVehicle.getDeliveryGuyID();
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentOutOfStock_ (defined as a static inner class below).



        if(position==0)
        {
            PendingHandoverFragment pendingHandoverFragment = PendingHandoverFragment.newInstance();
            pendingHandoverFragment.setDeliveryGuySelf(deliveryVehicle);
            return pendingHandoverFragment;
        }
        else if(position == 1)
        {
            OutForDeliveryFragment outForDeliveryFragment = OutForDeliveryFragment.newInstance();
            outForDeliveryFragment.setDeliveryGuySelf(deliveryVehicle);
            return outForDeliveryFragment;

        }else if (position == 2)
        {
            PendingDeliveryApproval fragment = PendingDeliveryApproval.newInstance();
            fragment.setDeliveryGuySelf(deliveryVehicle);
            return fragment;

        }
        else if (position == 3)
        {
            PaymentsPendingFragment paymentsPendingFragment = PaymentsPendingFragment.newInstance();
            paymentsPendingFragment.setDeliveryGuySelf(deliveryVehicle);
            return paymentsPendingFragment;
        }
        else if (position == 4)
        {
            PendingReturnDGI pendingReturnDGI = PendingReturnDGI.newInstance();
            pendingReturnDGI.setDeliveryGuySelf(deliveryVehicle);
            return pendingReturnDGI;
        }
        else if (position == 5)
        {
            PendingReturnCancelledByShopDGI returnCancelledByShopDGI = PendingReturnCancelledByShopDGI.newInstance();
            returnCancelledByShopDGI.setDeliveryGuySelf(deliveryVehicle);
            return returnCancelledByShopDGI;
        }


        return PlaceholderFragment.newInstance(vehicleID);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return titlePendingHandover;
            case 1:
                return titleConfirmed;
            case 2:
                return titleDeliveryApproval;
            case 3:
                return titlePendingPayments;
            case 4:
                return titlePendingReturn;
            case 5:
                return titlePendingReturnCancelledShop;

        }
        return null;
    }





    private String titlePendingHandover = "Pending Handover ( 0/0 )";
    private String titleConfirmed = "Out For Delivery ( 0 / 0 )";
    private String titleDeliveryApproval = "Pending Delivery Approval (0/0)";
    private String titlePendingPayments = "Pending Payments (0/0)";
    private String titlePendingReturn = "Pending Return (0/0)";
    private String titlePendingReturnCancelledShop = "Pending Return (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titlePendingHandover = title;
        }
        else if (tabPosition == 1)
        {
            titleConfirmed = title;

        }else if(tabPosition == 2)
        {
            titleDeliveryApproval = title;

        }else if(tabPosition == 3)
        {
            titlePendingPayments = title;
        }
        else if(tabPosition == 4)
        {
            titlePendingReturn = title;
        }
        else if(tabPosition == 5)
        {
            titlePendingReturnCancelledShop = title;
        }

        notifyDataSetChanged();
    }

}