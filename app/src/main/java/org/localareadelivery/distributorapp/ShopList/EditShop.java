package org.localareadelivery.distributorapp.ShopList;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditShop extends AppCompatActivity implements LocationListener {


    public static final String INTENT_EXTRA_SHOP_KEY = "intentExtraShopKey";


    @Bind(R.id.shopID)
    EditText shopID;
    @Bind(R.id.shopName)
    EditText shopName;
    @Bind(R.id.radiusOfService)
    EditText radiusOfService;
    @Bind(R.id.latitude)
    EditText latitude;
    @Bind(R.id.longitude)
    EditText longitude;
    @Bind(R.id.averageRating)
    EditText averageRating;
    @Bind(R.id.deliveryCharges)
    EditText deliveryCharge;
    @Bind(R.id.distributorID)
    EditText distributorID;

    @Bind(R.id.addShopButton)
    Button addShopButton;
    @Bind(R.id.result)
    TextView result;

    @Bind(R.id.getCurrentLocationButton)
    Button getCurrentLocation;

    //GoogleApiClient mGoogleApiClient;


    Shop shop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {

            shop = getIntent().getExtras().getParcelable(INTENT_EXTRA_SHOP_KEY);
        }


        bindData();



        /*
        // for getting the current Location
        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        */


        //requestPermission();

    }


    void bindData() {

        if (shop != null) {
            shop.setDistributorID(getDistributorID());

            Log.d("applog", shop.toString());
            shopID.setText(String.valueOf(shop.getShopID()));
            shopName.setText(shop.getShopName());
            radiusOfService.setText(String.valueOf(shop.getRadiusOfService()));
            latitude.setText(String.valueOf(shop.getLatitude()));
            longitude.setText(String.valueOf(shop.getLongitude()));
            averageRating.setText(String.valueOf(shop.getAverageRating()));
            deliveryCharge.setText(String.valueOf(shop.getDeliveryCharges()));
            distributorID.setText(String.valueOf(shop.getDistributorID()));

        }
    }


    void getDataFromEditText() {
        //if(shop!=null) {

        shop.setShopName(shopName.getText().toString());

//            try {
        shop.setShopID(Integer.parseInt(shopID.getText().toString()));
        shop.setRadiusOfService(Double.parseDouble(radiusOfService.getText().toString()));
        shop.setLatitude(Double.parseDouble(latitude.getText().toString()));
        shop.setLongitude(Double.parseDouble(longitude.getText().toString()));
        shop.setAverageRating(Double.parseDouble(averageRating.getText().toString()));
        shop.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        //          } catch (Exception ex) {

        //        }
        //        }


    }


    void makePUTRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        // store the recently updated data to the shop object
        getDataFromEditText();

        Call<ResponseBody> shopCall = shopService.updateShop(shop, shop.getShopID());

        shopCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                response.body();

                ResponseBody responseBody = response.body();

                Log.d("applog", String.valueOf(response.isSuccessful()) + response.toString());

                displayResult();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.addShopButton)
    void updateShop() {
        makePUTRequest();
    }


    void displayResult() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        Call<Shop> shopCall = shopService.getShop(shop.getShopID());

        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {


                EditShop.this.result.setText(response.body().toString());

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });



        /*
        String resultMessage = "ID: " + shop.getShopID()
                + "\nSHOP NAME : " + shop.getShopName()
                + "\nRADIUS OF SERVICE : " + shop.getRadiusOfService()
                + "\nLATITUDE : " + shop.getLatitude()
                + "\nLONGITUDE : " + shop.getLongitude()
                + "\nAVERAGE RATING : " + shop.getAverageRating()
                + "\nDELIVERY CHARGES : " + shop.getDeliveryCharges()
                + "\nDISTRIBUTOR ID : " + shop.getDistributorID();
        */
    }


    public int getDistributorID() {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key), 0);

        return distributorID;
    }


    public String getServiceURL() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key), "default");

        return service_url;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    LocationManager mlocationManager;

    @OnClick(R.id.getCurrentLocationButton)
    public void requestPermission() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

            return;

        }

        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
    }



    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                }
                break;
        }


    }

    // instance variable for storing the last location
    Location mLastLocation;

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


        LocationRequest locationRequest = new LocationRequest();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);


        if (mLastLocation != null) {
            longitude.setText(String.valueOf(mLastLocation.getLongitude()));
            latitude.setText(String.valueOf(mLastLocation.getLatitude()));
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }
    }
    */


    @Override
    protected void onStart() {
        super.onStart();

//        mGoogleApiClient.connect();


    }

    @Override
    protected void onStop() {
        super.onStop();

        //      mGoogleApiClient.disconnect();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if(mlocationManager!=null)
        {

            mlocationManager.removeUpdates(this);
        }
    }



    @Override
    public void onLocationChanged(Location location) {


        if(location!=null)
        {



        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;

        }

            mlocationManager.removeUpdates(this);

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}
