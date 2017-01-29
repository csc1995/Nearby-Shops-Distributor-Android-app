package org.nearbyshops.shopkeeperapp.OrderDetailPFS;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.shopkeeperapp.ModelPickFromShop.OrderPFS;
import org.nearbyshops.shopkeeperapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 19/10/16.
 */




public class UtilityOrderDetailPFS {


    public static final String TAG_ORDER_DETAIL = "ORDER_DETAIL_PFS";

    public static void saveOrder(OrderPFS order, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        if(order == null)
        {
            prefsEditor.putString(TAG_ORDER_DETAIL, "null");

        }
        else
        {
            Gson gson = new Gson();
            String json = gson.toJson(order);
            prefsEditor.putString(TAG_ORDER_DETAIL, json);
        }

        prefsEditor.apply();
    }


    public static OrderPFS getOrder(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_ORDER_DETAIL, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, OrderPFS.class);
        }

    }
}
