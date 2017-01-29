package org.nearbyshops.shopkeeperapp.ShopStaffAccounts;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityShopStaff {

    private static final String TAG_PREF_STAFF = "shop_staff";

    public static void saveShopStaff(ShopStaff staff, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(staff);
        prefsEditor.putString(TAG_PREF_STAFF, json);
        prefsEditor.apply();
    }


    public static ShopStaff getShopStaff(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TAG_PREF_STAFF, "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff.class);
        }
    }


}
