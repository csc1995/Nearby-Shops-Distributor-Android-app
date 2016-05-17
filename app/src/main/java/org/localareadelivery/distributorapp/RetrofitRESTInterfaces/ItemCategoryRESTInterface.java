package org.localareadelivery.distributorapp.RetrofitRESTInterfaces;

import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemCategoryService;
import org.localareadelivery.distributorapp.StandardInterfaces.DataSubscriber;
import org.localareadelivery.distributorapp.StandardInterfaces.RESTInterface;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumeet on 17/5/16.
 */
public class ItemCategoryRESTInterface implements RESTInterface<ItemCategory>{

    public final String PARENT_ID_PARAM = "ParentID";
    public final String SHOP_ID = "ShopID";

    @Inject Retrofit retrofit;

    ItemCategoryService itemCategoryService;


    public ItemCategoryRESTInterface() {

        itemCategoryService = retrofit.create(ItemCategoryService.class);
    }


    @Override
    public void get(int ID, final DataSubscriber.ReadCallback<ItemCategory> callback) {

        Call<ItemCategory> call = itemCategoryService.getItemCategory(ID);

        call.enqueue(new Callback<ItemCategory>() {

            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                callback.readCallback(false,true,response.code(),response.body());
            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                callback.readCallback(false,true,-1,null);

            }
        });


    }

    @Override
    public void getMany(
            Map<String, String> stringParams,
            Map<String, Integer> intParams,
            Map<String, Boolean> booleanParams,
            final DataSubscriber.ReadManyCallback<ItemCategory> callback
    ) {

        Call<List<ItemCategory>> call = itemCategoryService
                .getItemCategories(
                        intParams.get(PARENT_ID_PARAM),
                        intParams.get(SHOP_ID)
                );

        call.enqueue(new Callback<List<ItemCategory>>() {
            @Override
            public void onResponse(Call<List<ItemCategory>> call, Response<List<ItemCategory>> response) {

                callback.readManyCallback(false,true,response.code(),response.body());
            }

            @Override
            public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                callback.readManyCallback(false,false,-1,null);

            }
        });

    }

    @Override
    public void delete(int ID, final DataSubscriber.DeleteCallback callback) {

        Call<ResponseBody> call = itemCategoryService.deleteItemCategory(ID);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                callback.deleteShopCallback(false,true,response.code());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                callback.deleteShopCallback(false,true,-1);
            }
        });

    }

    @Override
    public void post(ItemCategory itemCategory, final DataSubscriber.CreateCallback<ItemCategory> callback) {


        Call<ItemCategory> call = itemCategoryService.insertItemCategory(itemCategory);

        call.enqueue(new Callback<ItemCategory>() {
            @Override
            public void onResponse(Call<ItemCategory> call, Response<ItemCategory> response) {

                callback.createCallback(false,true,response.code(),response.body());
            }

            @Override
            public void onFailure(Call<ItemCategory> call, Throwable t) {

                callback.createCallback(false,false,-1,null);
            }
        });

    }

    @Override
    public void put(ItemCategory itemCategory, final DataSubscriber.UpdateCallback callback) {


        Call<ResponseBody> call = itemCategoryService
                .updateItemCategory(itemCategory,itemCategory.getItemCategoryID());

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                callback.updateCallback(false,true,response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                callback.updateCallback(false,false,-1);
            }
        });

    }

}
