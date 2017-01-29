package org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingDeliveryApproval.PendingDeliveryApprovalDGD;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingHandover.PendingHandoverFragment;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.OutForDelivery.FragmentOutForDelivery;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingPayments.PaymentsPendingFragment;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturn.PendingReturnByDG;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturnCancelledByShop.PendingReturnCancelledByShop;
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.PendingReturnCancelledByUser.PendingReturnCancelledByUser;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;

/**
 * Created by sumeet on 15/6/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {

//    DeliveryGuySelf deliveryVehicle;
//
//    int vehicleID;







    public PagerAdapter(FragmentManager fm, DeliveryGuySelf deliveryGuySelf) {
        super(fm);

//        deliveryVehicle = deliveryGuySelf;
//
//        if(deliveryVehicle!=null)
//        {
//            vehicleID = deliveryVehicle.getDeliveryGuyID();
//        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FragmentOutOfStock_ (defined as a static inner class below).



        if(position==0)
        {
//            PendingHandoverFragment pendingHandoverFragment = PendingHandoverFragment.newInstance();
//            pendingHandoverFragment.setDeliveryGuySelf(deliveryVehicle);
            return PendingHandoverFragment.newInstance();

        }else if(position == 1)
        {
//            FragmentOutForDelivery fragmentOutForDelivery = FragmentOutForDelivery.newInstance();
//            fragmentOutForDelivery.setDeliveryGuySelf(deliveryVehicle);
            return FragmentOutForDelivery.newInstance();
        }
        else if(position == 2)
        {
//            PendingDeliveryApprovalDGD pendingDeliveryApproval = PendingDeliveryApprovalDGD.newInstance();
//            pendingDeliveryApproval.setDeliveryGuySelf(deliveryVehicle);
            return PendingDeliveryApprovalDGD.newInstance();
        }
        else if(position == 3)
        {
//            PaymentsPendingFragment paymentsPendingFragment;
//            paymentsPendingFragment = PaymentsPendingFragment.newInstance();
//            paymentsPendingFragment.setDeliveryGuySelf(deliveryVehicle);
            return PaymentsPendingFragment.newInstance();
        }
        else if(position == 4)
        {
//            PendingReturnByDG pendingReturnByDG;
//            pendingReturnByDG = PendingReturnByDG.newInstance();
//            pendingReturnByDG.setDeliveryGuySelf(deliveryVehicle);
            return PendingReturnByDG.newInstance();
        }
        else if(position == 5)
        {
//            PendingReturnCancelledByUser returnCancelledByShop;
//            returnCancelledByShop = PendingReturnCancelledByUser.newInstance();
//            returnCancelledByShop.setDeliveryGuySelf(deliveryVehicle);
            return PendingReturnCancelledByShop.newInstance();
        }
        else if(position==6)
        {
            return PendingReturnCancelledByUser.newInstance();
        }


        return null;
//        return PlaceholderFragment.newInstance(vehicleID);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 7;
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
            case 6:
                return titlePendingReturnCancelledUser;

        }
        return null;
    }



            /*case 0:
                        return "Pending Handover";
            case 1:
                    return "Out for Delivery";
            case 2:
                    return "Pending Payments";
            case 3:
                    return "Pending Delivery";
            case 4:
                    return "Pending Return (Cancelled by User)";
            case 5:
                    return  "Pending Return (Cancelled by Shop Owner)";
            case 6:
                    return "Pending Return (Returned by Delivery Guy)";
*/




    private String titlePendingHandover = "Pending Handover ( 0/0 )";
    private String titleConfirmed = "Out For Delivery ( 0 / 0 )";
    private String titleDeliveryApproval = "Pending Delivery Approval (0/0)";
    private String titlePendingPayments = "Pending Payments (0/0)";
    private String titlePendingReturn = "Pending Return (0/0)";
    private String titlePendingReturnCancelledShop = "Pending Return (0/0)";
    private String titlePendingReturnCancelledUser = "Pending Return (0/0)";


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
        else if(tabPosition==6)
        {
            titlePendingReturnCancelledUser = title;
        }


        notifyDataSetChanged();
    }
}