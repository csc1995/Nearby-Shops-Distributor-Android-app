package org.localareadelivery.distributorapp.DAOs;

import android.support.annotation.NonNull;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.http.QueryMap;


/**
 * Created by sumeet on 12/5/16.
 */
public class ShopDAO {

    static ShopDAO shopDAO;

    @Inject ShopRESTContract shopRESTInterface;

    //CRUD - Create, Read, Update, Delete
    //S/IRUD - Save/Insert, Read, Update, Delete
    //REST terminology - Post, Get, Put, Delete


    // Purpose of this Class: This class provides a standard DAO(Data Access Object) interface for the consumers of the Data.
    // For more information about DAO design patter you can do a google search with the keywords "DAO design Pattern".
    // The benefit of using a standard DAO interface would enable us to replace the HTTP Libraries,
    // ORM or Database access implementation without affecting the majority of application code.
    // For example: If you use DAO for accessing data. Then you can easily replace your HTTP library. For the change
    // you would have to make changes in the DAO class only instead of making changes in the entire application code.
    // Therefore it introduces a loose coupling in a way that the Application code is not tightly coupled with the
    // code for http calls and the code for database calls.

    // A simple impementation of this DAO would follow a strategy where. The DAO delivers the data from the HTTP REST call,
    // in the presence of INternet connection. We will call this Online Mode.
    // When there is no interent connection. It will simply try to return the result from the stored database.
    // And when both the approach fails in case the data retrival from database also fails the DAO would simply give a
    // failed response.



    // User Interface Response
    // Offline and No data Available: Application is Offline. No Data Available !!!


    private ShopDAO() {

        //shopRESTContract = ShopRESTInterface.getInstance();
        //shopRESTContract= VolleyShopCalls.getInstance();

        //ApplicationState.getInstance()
        //        .getMyApplication()
        //        .getRestComponent().inject(this);

        DaggerComponentBuilder.getInstance()
                .getRestComponent().inject(this);

    }

    public static ShopDAO getInstance()
    {
        if(shopDAO == null)
        {
            shopDAO = new ShopDAO();
        }

        return shopDAO;
    }



    public void createShop(Shop shop,CreateShopCallback createShopCallback) {

        if (UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext())) {

            // Network Available !

            shopRESTInterface.postShop(shop,createShopCallback);


        } else
        {

            // No network Available !

            createShopCallback.createShopCallback(true,false,-1,null);



        }
    }



    public void updateShop(Shop shop,UpdateShopCallback updateShopCallback) {

        if (UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // make a rest HTTP call and deliver the results

            shopRESTInterface.putShop(shop,updateShopCallback);
        }
        else
        {
            // the network is not available. So make a database call or just deliver an unsuccessful message.

            updateShopCallback.updateShopCallback(true,false,-1);
        }



    }// CreateShopEnds


    public void deleteShop(int shopID,DeleteShopCallback callback)
    {
        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            shopRESTInterface.deleteShop(shopID,callback);

        }else
        {
            callback.deleteShopCallback(true,false,-1);

        }
    }// delete Shop Ends




    public void readShops(int distributorID,ReadShopCallback readShopCallback)
    {
        if (UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // make a rest HTTP call and deliver the results

            shopRESTInterface.getShops(distributorID,readShopCallback);
        }
        else
        {
            // the network is not available. So make a database call or just deliver an unsuccessful message.

            readShopCallback.readShopsCallback(true,false,-1,null);

        }

    }




    // DAO(Data Access Objects) callback interfaces.
    // The consumers of the DAO's need to implement these callback interfaces to get the results

    public interface CreateShopCallback{

        public void createShopCallback(boolean isOffline,boolean isSuccessful,int httpStatusCode, Shop shop);
    }

    public interface UpdateShopCallback{

        public void updateShopCallback(boolean isOffline, boolean isSuccessful,int httpStatusCode);
    }

    public interface DeleteShopCallback{

        public void deleteShopCallback( boolean isOffline,boolean isSuccessful, int httpStatusCode);
    }

    public interface ReadShopCallback{

        public void readShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode,Shop shop);
        public void readShopsCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<Shop> shops);
    }






    // A standard Interface which needs to be implemented by any REST service provider in order to be able
    // to be accessed by the DAO

    public interface ShopRESTContract{

        public void getShop(int shopID,ReadShopCallback callback);

        public void getShops(int distributorID, ReadShopCallback callback);

        public void deleteShop(int shopID, DeleteShopCallback callback);

        public void postShop(Shop shop, CreateShopCallback callback);

        public void putShop(Shop shop, UpdateShopCallback callback);

    }

}
