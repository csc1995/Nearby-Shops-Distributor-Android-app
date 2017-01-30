package org.nearbyshops.shopkeeperapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;

import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopAdmin;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.MyApplication;
import org.nearbyshops.shopkeeperapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sumeet on 25/9/16.
 */

public class UtilityLogin {


    public static final Integer ROLE_DISTRIBUTOR = 1;
    public static final Integer ROLE_STAFF = 2;
    public static final Integer ROLE_DELIVERY = 3;





    public static void saveCredentials(Context context, String username, String password)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
    }




    public static String getUsername(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("username", "");
        return service_url;
    }

    public static String getPassword(Context context)
    {
        context = MyApplication.getAppContext();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        String service_url = sharedPref.getString("password", "");
        return service_url;
    }



    public static void setRoleID(Context context, int role_id)
    {
        // get a handle to shared Preference
        SharedPreferences sharedPref;

        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name),
                MODE_PRIVATE);

        // write to the shared preference
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("role", role_id);

        editor.apply();
    }


    public static int getRoleID(Context context) {

        context = MyApplication.getAppContext();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        int role_id = sharedPref.getInt("role", -1);
        return role_id;
    }



    public static String baseEncoding(String username,String password)
    {
        String credentials = username + ":" + password;
        // create Base64 encodet string
        String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return basic;
    }


    public static String getAuthorizationHeaders(Context context)
    {
        return UtilityLogin.baseEncoding(
                UtilityLogin.getUsername(context),
                UtilityLogin.getPassword(context));


//        if(getRoleID(context)==ROLE_DISTRIBUTOR)
//        {
//            Distributor distributor = UtilityLogin.getDistributor(context);
//
//            if(distributor!=null)
//            {
//            }
//        }
//        return null;

    }




//    public static void saveDistributor(Distributor endUser, Context context)
//    {
//
//        if(context == null)
//        {
//            return;
//        }
//
//        //Creating a shared preference
//
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        SharedPreferences.Editor prefsEditor = sharedPref.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(endUser);
//        prefsEditor.putString("distributor", json);
//        prefsEditor.apply();
//    }
//
//
//    public static Distributor getDistributor(Context context)
//    {
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
//
//        Gson gson = new Gson();
//        String json = sharedPref.getString("distributor", "null");
//
//        if(json.equals("null"))
//        {
//
//            return null;
//
//        }else
//        {
//            return gson.fromJson(json, Distributor.class);
//        }
//    }


    public static void saveDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deliveryGuySelf);
        prefsEditor.putString("deliveryGuySelf", json);
        prefsEditor.apply();
    }


    public static DeliveryGuySelf getDeliveryGuySelf(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("deliveryGuySelf", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, DeliveryGuySelf.class);
        }
    }



    public static void saveShopAdmin(ShopAdmin shopAdmin, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shopAdmin);
        prefsEditor.putString("shopAdmin", json);
        prefsEditor.apply();
    }


    public static ShopAdmin getShopAdmin(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("shopAdmin", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ShopAdmin.class);
        }
    }


    public static void saveShopStaff(ShopStaff shopStaff, Context context)
    {

        //Creating a shared preference

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shopStaff);
        prefsEditor.putString("shopStaff", json);
        prefsEditor.apply();
    }


    public static ShopStaff getShopStaff(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString("shopStaff", "null");

        if(json.equals("null"))
        {

            return null;

        }else
        {
            return gson.fromJson(json, ShopStaff.class);
        }
    }

}
