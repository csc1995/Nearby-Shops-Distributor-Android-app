package org.nearbyshops.shopkeeperapp.DaggerModules;

import android.app.Application;

import org.nearbyshops.shopkeeperapp.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 14/5/16.
 */

@Module
public class AppModule {

    MyApplication mApplication;

    public AppModule(MyApplication application) {
        mApplication = application;
    }


    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}
