package org.localareadelivery.distributorapp.ApplicationState;

import org.localareadelivery.distributorapp.Model.Shop;

/**
 * Created by sumeet on 15/3/16.
 */
public class ApplicationState {

    static ApplicationState instance = null;

    Shop currentShop = null;


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
        return currentShop;
    }

    public void setCurrentShop(Shop currentShop) {
        this.currentShop = currentShop;
    }

}
