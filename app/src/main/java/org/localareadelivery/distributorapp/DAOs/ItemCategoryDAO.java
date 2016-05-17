package org.localareadelivery.distributorapp.DAOs;

import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.StandardInterfaces.DAO;
import org.localareadelivery.distributorapp.StandardInterfaces.DataSubscriber;
import org.localareadelivery.distributorapp.StandardInterfaces.RESTInterface;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import java.util.Map;

/**
 * Created by sumeet on 14/5/16.
 */
public class ItemCategoryDAO implements DAO<ItemCategory> {

    private static

    RESTInterface<ItemCategory> restInterface;


    public ItemCategoryDAO() {
    }

    public static ItemCategoryDAO getInstance()
    {

        return null;
    }


    @Override
    public void create(ItemCategory itemCategory, DataSubscriber.CreateCallback<ItemCategory> createCallback) {

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            //online mode
            restInterface.post(itemCategory,createCallback);

        }else
        {

            // offline mode
            createCallback.createCallback(true,false,-1,null);

        }


    }

    @Override
    public void update(ItemCategory itemCategory, DataSubscriber.UpdateCallback updateCallback) {

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // online Mode

            restInterface.put(itemCategory,updateCallback);

        }else
        {
            // offline Mode

            updateCallback.updateCallback(true,false,-1);
        }

    }

    @Override
    public void delete(int ID, DataSubscriber.DeleteCallback deleteCallback) {

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            restInterface.delete(ID,deleteCallback);

        }else
        {
            // Offline Mode

            deleteCallback.deleteShopCallback(true,false,-1);

        }

    }

    @Override
    public void read(int ID, DataSubscriber.ReadCallback<ItemCategory> readCallback) {

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            restInterface.get(ID,readCallback);

        }
        else
        {

        }
    }

    @Override
    public void readMany(Map<String, String> queryParams, DataSubscriber.ReadManyCallback readManyCallback) {

    }


}
