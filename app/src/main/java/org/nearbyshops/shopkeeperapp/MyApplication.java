package org.nearbyshops.shopkeeperapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

//import com.squareup.leakcanary.LeakCanary;

import org.nearbyshops.shopkeeperapp.ApplicationState.ApplicationState;

/**
 * Created by sumeet on 12/5/16.
 */
public class MyApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    private static Context context;

    public void onCreate() {

        super.onCreate();

        MyApplication.context = getApplicationContext();

//        LeakCanary.install(this);

        ApplicationState.getInstance().setMyApplication(this);
    }


    public static Context getAppContext() {

        return MyApplication.context;


    }


}
