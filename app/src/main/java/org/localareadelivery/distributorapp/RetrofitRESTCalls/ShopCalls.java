package org.localareadelivery.distributorapp.RetrofitRESTCalls;

import android.content.Context;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopService;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 12/5/16.
 */
public class ShopCalls {

    static ShopCalls shopCalls;


    ShopService shopService;

    Retrofit retrofit;


    private ShopCalls(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    private ShopCalls(Context context) {

        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        shopService = retrofit.create(ShopService.class);
    }


    public static ShopCalls getInstance()
    {

        if(shopCalls == null)
        {
            shopCalls = new ShopCalls(MyApplication.getAppContext());
        }

        return shopCalls;
    }



    // Do not forget to update the Shop before making the PUT request
    public void putShopRequest(Shop shop, Callback<ResponseBody> updateShopCallback) {

        // store the recently updated data to the shop object
        Call<ResponseBody> shopCall = shopService.putShop(shop, shop.getShopID());

        shopCall.enqueue(updateShopCallback);
    }


    public void postShop()
    {

    }


    public void deleteShop()
    {

    }


    // get http REST calls

    public void getShop()
    {

    }


    public void getShops()
    {

    }



}
