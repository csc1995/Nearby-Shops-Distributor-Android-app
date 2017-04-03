package org.nearbyshops.shopkeeperapp.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nearbyshops.shopkeeperapp.MyApplication;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DistributorService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderServiceDeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderServiceShopStaff;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopAdminService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopStaffService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractPFS.OrderServiceShopStaffPFS;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSDS.ServiceConfigService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions.ItemImageSubmissionService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions.ItemSubmissionService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemImageService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemSpecItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemSpecNameService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemSpecValueService;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 14/5/16.
 */

        /*
        retrofit = new Retrofit.Builder()
                .baseUrl(UtilityGeneral.getServiceURL(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

@Module
public class NetModule {

    String serviceURL;

    // Constructor needs one parameter to instantiate.
    public NetModule() {

    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    /*
    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    */

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


//        .setDateFormat("yyyy-MM-dd hh:mm:ss.S")
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        // Cache cache

        // cache is commented out ... you can add cache by putting it back in the builder options
        //.cache(cache)

        return client;
    }


    //    @Singleton

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

//        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        return retrofit;
    }




    @Provides @Named("sds")
    Retrofit provideRetrofitGIDB(Gson gson, OkHttpClient okHttpClient) {

        //        .client(okHttpClient)

//        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL_SDS(MyApplication.getAppContext()));


        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL_SDS(MyApplication.getAppContext()))
                .build();
    }



    @Provides
    ServiceConfigService provideServiceConfig(@Named("sds")Retrofit retrofit)
    {
        return retrofit.create(ServiceConfigService.class);
    }





    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
        OrderService service = retrofit.create(OrderService.class);

        return service;
    }



    @Provides
    ShopItemService provideShopItemService(Retrofit retrofit)
    {
        ShopItemService service = retrofit.create(ShopItemService.class);

        return service;
    }


    @Provides
    DeliveryGuySelfService provideDeliveryGuyService(Retrofit retrofit)
    {
        return retrofit.create(DeliveryGuySelfService.class);
    }


    @Provides
    ShopAdminService provideShopAdmin(Retrofit retrofit)
    {
        return retrofit.create(ShopAdminService.class);
    }


    @Provides
    ItemService provideItemService(Retrofit retrofit)
    {
        ItemService service = retrofit.create(ItemService.class);

        return service;
    }

    @Provides
    ItemCategoryService provideItemCategory(Retrofit retrofit)
    {
        ItemCategoryService service = retrofit.create(ItemCategoryService.class);

        return service;
    }


    @Provides
    ShopService provideShopService(Retrofit retrofit)
    {
        ShopService service = retrofit.create(ShopService.class);

        return service;
    }



    @Provides
    DistributorService provideDistributorService(Retrofit retrofit)
    {
        return retrofit.create(DistributorService.class);
    }


    @Provides
    OrderItemService provideOrderItemService(Retrofit retrofit)
    {
        return retrofit.create(OrderItemService.class);
    }


    @Provides
    OrderServiceShopStaff provideOrderShopStaff(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceShopStaff.class);
    }


    @Provides
    ShopStaffService provideShopStaffService(Retrofit retrofit)
    {
        return retrofit.create(ShopStaffService.class);
    }



    @Provides
    OrderServiceDeliveryGuySelf provideOrderServiceDelivery(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceDeliveryGuySelf.class);
    }



    @Provides
    OrderServiceShopStaffPFS provideOrderServicePFS(Retrofit retrofit)
    {
        return retrofit.create(OrderServiceShopStaffPFS.class);
    }






    @Provides
    ItemImageService provideItemImageService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageService.class);
    }


    @Provides
    ItemSpecItemService provideItemSpecItem(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecItemService.class);
    }

    @Provides
    ItemSpecNameService provideSpecNameService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecNameService.class);
    }



    @Provides
    ItemSpecValueService provideSpecValueService(Retrofit retrofit)
    {
        return retrofit.create(ItemSpecValueService.class);
    }


    @Provides
    ItemSubmissionService provideItemSubmissionService(Retrofit retrofit)
    {
        return retrofit.create(ItemSubmissionService.class);
    }

    @Provides
    ItemImageSubmissionService provideItemImageSubmissionService(Retrofit retrofit)
    {
        return retrofit.create(ItemImageSubmissionService.class);
    }



}
