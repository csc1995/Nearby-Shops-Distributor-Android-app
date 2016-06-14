package org.localareadelivery.distributorapp.VolleyRESTCalls;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;
import org.localareadelivery.distributorapp.Utility.VolleySingleton;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sumeet on 13/5/16.
 */
public class VolleyShopCalls implements ShopDAO.ShopRESTContract{

    String serviceURL;

    static VolleyShopCalls instance;


    private VolleyShopCalls() {
        this.serviceURL = UtilityGeneral.getServiceURL(MyApplication.getAppContext());
    }

    public static VolleyShopCalls getInstance()
    {

        if(instance == null)
        {
            instance = new VolleyShopCalls();
        }

        return instance;
    }


    @Override
    public void getShop(int shopID, ShopDAO.ReadShopCallback callback) {



    }

    @Override
    public void getShops(int distributorID, final ShopDAO.ReadShopCallback callback) {

        String url = serviceURL + "/api/Shop" + "?DistributorID=" + String.valueOf(distributorID);

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);
                try {

                    Gson gson = new GsonBuilder().create();

                    Type listType = new TypeToken<List<Shop>>() {}.getType();
                    List<Shop> parsedItems = gson.fromJson(response,listType);

                    callback.readShopsCallback(false,true,-1,parsedItems);


                } catch (Exception e) {
                    e.printStackTrace();

                    callback.readShopsCallback(false,false,-1,null);


                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());

                callback.readShopsCallback(false,false,-1,null);

            }
        });

        VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);

    }

    @Override
    public void deleteShop(int shopID, ShopDAO.DeleteShopCallback callback) {

    }

    @Override
    public void postShop(Shop shop, ShopDAO.CreateShopCallback callback) {

    }

    @Override
    public void putShop(Shop shop, final ShopDAO.UpdateShopCallback callback) {


    }


}
