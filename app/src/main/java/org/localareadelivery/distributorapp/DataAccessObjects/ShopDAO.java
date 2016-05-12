package org.localareadelivery.distributorapp.DataAccessObjects;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.RetrofitRESTCalls.ShopCalls;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 12/5/16.
 */
public class ShopDAO {

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



    public void updateShop(Shop shop,UpdateShopCallback updateShopCallback) {

        if (UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // make a rest HTTP call and deliver the results

            ShopCalls shopCalls = ShopCalls.getInstance();

            PutShopCallback.callback = updateShopCallback;

            shopCalls.putShopRequest(shop, new PutShopCallback());
        }

        else

        {
            // the network is not available. So make a database call or just deliver an unsuccessful message.

            updateShopCallback.updateShopCallback(false,true,-1);
        }


    }// CreateShopEnds



    static class PutShopCallback implements  Callback<ResponseBody>
    {
        static UpdateShopCallback callback;

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            callback.updateShopCallback(true,false,response.code());
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            callback.updateShopCallback(false,false,-1);
        }
    }







    // DAO(Data Access Objects) callback interfaces.
    // The consumers of the DAO's need to implement these callback interfaces to get the results

    interface CreateShopCallback{

        public void createShopCallback(boolean isSuccessful, boolean isOffline,int httpStatusCode, Shop shop);
    }

    interface UpdateShopCallback{

        public void updateShopCallback(boolean isSuccessful,boolean isOffline, int httpStatusCode);
    }

    interface DeleteShopCallback{

        public void deleteShopCallback(boolean isSuccessful, boolean isOffline, int httpStatusCode);
    }

    interface ReadShopCallback{

        public void readShopCallback(boolean isSuccessful, boolean isOffline, int httpStatusCode,Shop shop);
        public void readShopsCallback(boolean isSuccessful, boolean isOffline, int httpStatusCode, List<Shop> shops);
    }

}
