package org.localareadelivery.distributorapp.ShopList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class EditShop extends AppCompatActivity {


    public static final String INTENT_EXTRA_SHOP_KEY = "intentExtraShopKey";


    @Bind(R.id.shopID) EditText shopID;
    @Bind(R.id.shopName)EditText shopName;
    @Bind(R.id.radiusOfService) EditText radiusOfService;
    @Bind(R.id.latitude) EditText latitude;
    @Bind(R.id.longitude) EditText longitude;
    @Bind(R.id.averageRating) EditText averageRating;
    @Bind(R.id.deliveryCharges) EditText deliveryCharge;
    @Bind(R.id.distributorID) EditText distributorID;

    @Bind(R.id.addShopButton) Button addShopButton;
    @Bind(R.id.result) TextView result;

    Shop shop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        shop = getIntent().getExtras().getParcelable(INTENT_EXTRA_SHOP_KEY);

        bindData();



    }


    void bindData(){

        if(shop!=null) {
            shop.setDistributorID(getDistributorID());

            Log.d("applog",shop.toString());
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


    void getDataFromEditText()
    {
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


    void makePUTRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        // store the recently updated data to the shop object
        getDataFromEditText();

        Call<Response> shopCall = shopService.updateShop(shop,shop.getShopID());

        shopCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {

                response.body();

                Response responseBody = response.body();

                Log.d("applog",String.valueOf(response.isSuccess()) + response.toString());

                displayResult();

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.addShopButton)
    void updateShop()
    {
        makePUTRequest();
    }


    void displayResult()
    {
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



    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
