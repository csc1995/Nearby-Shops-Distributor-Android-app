package org.localareadelivery.distributorapp.ShopList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddShop extends AppCompatActivity {


    @Bind(R.id.shopName) EditText shopName;
    @Bind(R.id.radiusOfService) EditText radiusOfService;
    @Bind(R.id.latitude) EditText latitude;
    @Bind(R.id.longitude) EditText longitude;
    @Bind(R.id.averageRating) EditText averageRating;
    @Bind(R.id.deliveryCharges) EditText deliveryCharges;
    @Bind(R.id.distributorID) EditText distributorID;

    @Bind(R.id.addShopButton) Button addShop;
    @Bind(R.id.result)TextView result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        distributorID.setText(String.valueOf(getDistributorID()));
    }


    void makeRequest(Shop shop)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        Call<Shop> shopCall = shopService.insertShop(shop);


        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                Shop shopResponse = response.body();

                displayResult(shopResponse);
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });


    }



    void displayResult(Shop shop)
    {
        String resultString = "ID : " + shop.getShopID()
                        +"\n : " + shop.getShopName()
                        +"\n : " + String.valueOf(shop.getLatitude());



        result.setText(resultString);
    }




    @OnClick(R.id.addShopButton)
    void addShop()
    {

        Shop shop = new Shop();

        shop.setDistributorID(getDistributorID());
        shop.setShopName(shopName.getText().toString());

        try {

            shop.setAverageRating(Double.parseDouble(averageRating.getText().toString()));
            shop.setDeliveryCharges(Double.parseDouble(averageRating.getText().toString()));
            shop.setLatitude(Double.parseDouble(latitude.getText().toString()));
            shop.setLongitude(Double.parseDouble(longitude.getText().toString()));
            shop.setRadiusOfService(Double.parseDouble(radiusOfService.getText().toString()));


        }catch (Exception ex)
        {

        }

        makeRequest(shop);
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
