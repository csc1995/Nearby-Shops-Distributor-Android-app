package org.localareadelivery.distributorapp.DaggerModules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.RetrofitRESTContract.OrderService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.VehicleSelfService;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
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
        return gsonBuilder.create();
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
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        //        .client(okHttpClient)

        Log.d("applog","Retrofit : " + UtilityGeneral.getServiceURL(MyApplication.getAppContext()));


        return retrofit;
    }


    @Provides
    OrderService provideOrderService(Retrofit retrofit)
    {
        OrderService service = retrofit.create(OrderService.class);

        return service;
    }


    @Provides
    VehicleSelfService provideVehicleService(Retrofit retrofit)
    {
        VehicleSelfService service = retrofit.create(VehicleSelfService.class);

        return service;
    }


}
