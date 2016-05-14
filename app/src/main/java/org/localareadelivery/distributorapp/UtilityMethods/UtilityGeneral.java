package org.localareadelivery.distributorapp.UtilityMethods;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.localareadelivery.distributorapp.R;

/**
 * Created by sumeet on 5/5/16.
 */
public class UtilityGeneral {


    public static int getDistributorID(Context context) {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(context.getString(R.string.preference_distributor_id_key), 0);
        return distributorID;
    }


    public static String getServiceURL(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);
        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key), "default");

        //service_url = "http://localareademo-env.ap-southeast-1.elasticbeanstalk.com";

        return service_url;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /*



    public String  getServiceURL()
    {
        // Get a handle to the shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
    }

     */

}
