package org.localareadelivery.distributorapp.ApplicationState;

import android.util.Log;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.MyApplication;

/**
 * Created by sumeet on 15/3/16.
 */
public class ApplicationState {

    static ApplicationState instance = null;

    Shop currentShop = null;

    MyApplication myApplication;


    private ApplicationState() {
    }


    public static ApplicationState getInstance()
    {

        if(instance == null)
        {
            instance = new ApplicationState();

            return instance;
        }

        return instance;
    }





    public Shop getCurrentShop() {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        return currentShop;
    }

    public void setCurrentShop(Shop currentShop) {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        this.currentShop = currentShop;
    }


    public MyApplication getMyApplication() {
        return myApplication;
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }
}
