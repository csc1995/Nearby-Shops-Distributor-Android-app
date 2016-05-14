package org.localareadelivery.distributorapp;

import android.app.Application;
import android.content.Context;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponents.DaggerNetComponent;
import org.localareadelivery.distributorapp.DaggerComponents.DaggerRESTComponent;
import org.localareadelivery.distributorapp.DaggerComponents.NetComponent;
import org.localareadelivery.distributorapp.DaggerComponents.RESTComponent;
import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends Application{

    private static Context context;

    public void onCreate() {
        super.onCreate();

        MyApplication.context = getApplicationContext();









        ApplicationState.getInstance().setMyApplication(this);

    }


    public static Context getAppContext() {

        return MyApplication.context;


    }








}
