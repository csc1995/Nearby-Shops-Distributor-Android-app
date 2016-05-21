package org.localareadelivery.distributorapp;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponents.DAOComponent;

import org.localareadelivery.distributorapp.DaggerComponents.DaggerDAOComponent;
import org.localareadelivery.distributorapp.DaggerComponents.DaggerDataComponent;
import org.localareadelivery.distributorapp.DaggerComponents.DaggerNetComponent;
import org.localareadelivery.distributorapp.DaggerComponents.DaggerRESTComponent;
import org.localareadelivery.distributorapp.DaggerComponents.DataComponent;
import org.localareadelivery.distributorapp.DaggerComponents.NetComponent;
import org.localareadelivery.distributorapp.DaggerComponents.RESTComponent;
import org.localareadelivery.distributorapp.DaggerModules.AppModule;
import org.localareadelivery.distributorapp.DaggerModules.NetModule;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

/**
 * Created by sumeet on 14/5/16.
 */
public class DaggerComponentBuilder {


    private static DaggerComponentBuilder instance;

    private NetComponent mNetComponent;
    private RESTComponent restComponent;
    private DAOComponent daoComponent;

    private DataComponent dataComponent;


    private DaggerComponentBuilder() {
    }

    public static DaggerComponentBuilder getInstance()
    {
        if(instance == null)
        {
            instance = new DaggerComponentBuilder();
        }

        return instance;
    }


    public NetComponent getNetComponent() {

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mAppComponent = com.codepath.dagger.components.DaggerNetComponent.create();


        if(mNetComponent == null)
        {
            // Dagger%COMPONENT_NAME%
            mNetComponent = DaggerNetComponent.builder()
                    // list of modules that are part of this component need to be created here too
                    .appModule(new AppModule(ApplicationState
                            .getInstance().getMyApplication())) // This also corresponds to the name of your module: %component_name%Module
                    .netModule(new NetModule())
                    .build();
        }

        return mNetComponent;
    }


    public RESTComponent getRestComponent()
    {

        if(restComponent == null) {

            restComponent = DaggerRESTComponent.create();
        }

        return restComponent;
    }


    public DAOComponent getDaoComponent()
    {
        if(daoComponent == null)
        {
            daoComponent = DaggerDAOComponent.create();
        }

        return daoComponent;
    }


    public DataComponent getDataComponent()
    {
        if(dataComponent == null)
        {
            dataComponent = DaggerDataComponent.create();

        }

        return dataComponent;
    }

}
