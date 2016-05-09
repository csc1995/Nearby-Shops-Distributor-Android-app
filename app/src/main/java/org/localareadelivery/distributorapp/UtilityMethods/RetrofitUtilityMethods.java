package org.localareadelivery.distributorapp.UtilityMethods;

import android.content.Context;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ServiceContractRetrofit.ImageService;
import org.localareadelivery.distributorapp.ServiceContractRetrofit.ShopService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 5/5/16.
 */
public class RetrofitUtilityMethods {



    // Do not forget to update the Shop before making the PUT request
    public static void UpdateShopPUTRequest(Context context, Shop shop, Callback<ResponseBody> updateShopCallback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        // store the recently updated data to the shop object
        Call<ResponseBody> shopCall = shopService.updateShop(shop, shop.getShopID());

        shopCall.enqueue(updateShopCallback);

    }




    public static void deleteImage(String imageFileServerPath,Context context,Callback<ResponseBody> deleteImageCallback)
    {


        if (imageFileServerPath.length() > 2) {

            imageFileServerPath = imageFileServerPath.substring(1);

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);

        Call<ResponseBody> response = imageService.deleteImage(imageFileServerPath);

        response.enqueue(deleteImageCallback);

    }
}
