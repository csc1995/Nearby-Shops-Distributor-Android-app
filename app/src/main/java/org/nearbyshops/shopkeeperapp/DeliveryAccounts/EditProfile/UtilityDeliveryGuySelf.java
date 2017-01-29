package org.nearbyshops.shopkeeperapp.DeliveryAccounts.EditProfile;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityDeliveryGuySelf {

    private static final String TAG_PREF_DELIVERY = "delivery_guy_self";
    private static final String TAG_PREF_DELIVERY_ORDER_SELECTION = "delivery_guy_self_order_selection";

    public static void saveDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deliveryGuySelf);
        prefsEditor.putString(TAG_PREF_DELIVERY, json);
        prefsEditor.apply();
    }


    public static DeliveryGuySelf getDeliveryGuySelf(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_DELIVERY, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, DeliveryGuySelf.class);
        }
    }


    public static void saveDeliveryGuySelfOrderSelection(DeliveryGuySelf deliveryGuySelf, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deliveryGuySelf);
        prefsEditor.putString(TAG_PREF_DELIVERY_ORDER_SELECTION, json);
        prefsEditor.apply();
    }


    public static DeliveryGuySelf getDeliveryGuySelfOrderSelection(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_DELIVERY_ORDER_SELECTION, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, DeliveryGuySelf.class);
        }
    }

}
