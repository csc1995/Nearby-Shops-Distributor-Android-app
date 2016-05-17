package org.localareadelivery.distributorapp.RetrofitRESTInterfaces;

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopService;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumeet on 12/5/16.
 */
public class ShopRESTInterface implements ShopDAO.ShopRESTContract{

    static ShopRESTInterface shopRESTInterface;


    ShopService shopService;

    @Inject Retrofit retrofit;


    private ShopRESTInterface() {


        //ApplicationState.getInstance()
        //        .getMyApplication()
        //        .getNetComponent()
        //        .inject(this);

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .inject(this);

        shopService = retrofit.create(ShopService.class);

    }


    public static ShopRESTInterface getInstance()
    {

        if(shopRESTInterface == null)
        {
            shopRESTInterface = new ShopRESTInterface();
        }



        return shopRESTInterface;
    }


    @Override
    public void getShop(int shopID , ShopDAO.ReadShopCallback callback) {

        Call<Shop> shopCall = shopService.getShop(shopID);

        shopCall.enqueue(null);
    }

    @Override
    public void getShops(int distributorID, final ShopDAO.ReadShopCallback callback) {


        Call<List<Shop>> shopCall = shopService.getShops(distributorID);

        shopCall.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {

                callback.readShopsCallback(false,true,response.code(),response.body());
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {

                callback.readShopsCallback(false,false,-1,null);

            }
        });

    }

    @Override
    public void deleteShop(int shopID, final ShopDAO.DeleteShopCallback callback) {


        Call<ResponseBody> shopCall = shopService.deleteShop(shopID);

        shopCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                callback.deleteShopCallback(false,true,response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                callback.deleteShopCallback(false,false,-1);

            }
        });

    }




    @Override
    public void postShop(Shop shop, final ShopDAO.CreateShopCallback callback) {


        final Shop shopResult = null;

        Call<Shop> shopCall = shopService.postShop(shop);

        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {


                if(response.body()!=null) {

                    callback.createShopCallback(false, true, response.code(), response.body());
                }
                else
                {
                    callback.createShopCallback(false, true, response.code(), null);

                }

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {


                callback.createShopCallback(false,false, -1, null);

            }
        });

    }

    // Do not forget to update the Shop before making the PUT request
    @Override
    public void putShop(Shop shop, final ShopDAO.UpdateShopCallback callback) {

        // store the recently updated data to the shop object
        Call<ResponseBody> shopCall = shopService.putShop(shop, shop.getShopID());

        shopCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                callback.updateShopCallback(false,true,response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                callback.updateShopCallback(false,false,-1);

            }
        });

    }
}
