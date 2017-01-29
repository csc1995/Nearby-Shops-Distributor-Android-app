package org.nearbyshops.shopkeeperapp.aaDeprecated_DeliveryGuy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.Model.Shop;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleSelfActivity extends AppCompatActivity implements View.OnClickListener, Callback<DeliveryGuySelf> {

    DeliveryGuySelf deliveryGuySelf;

    @Inject
    DeliveryGuySelfService deliveryGuySelfService;

    TextView addVehicleSelf;

    // address Fields


    @Bind(R.id.vehicleName)
    EditText vehicleName;




    public AddVehicleSelfActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_self);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind Views

        addVehicleSelf = (TextView) findViewById(R.id.addVehicleSelf);

        addVehicleSelf.setOnClickListener(this);


    }



    void getDataFromViews()
    {


        Shop shop = UtilityShopHome.getShop(this);

        deliveryGuySelf = new DeliveryGuySelf();

        deliveryGuySelf.setName(vehicleName.getText().toString());
        deliveryGuySelf.setShopID(shop.getShopID());

    }


    @Override
    public void onClick(View v) {

        getDataFromViews();


        if(deliveryGuySelf !=null)
        {
            Call<DeliveryGuySelf> call = deliveryGuySelfService.postVehicle(UtilityLogin.getAuthorizationHeaders(this),deliveryGuySelf);

            call.enqueue(this);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call<DeliveryGuySelf> call, Response<DeliveryGuySelf> response) {


        if (response != null && response.code() == 201) {
            showToastMessage("Added Successfully !");
        }
        else
        {
            showToastMessage("Unsuccessful !");
        }

    }

    @Override
    public void onFailure(Call<DeliveryGuySelf> call, Throwable t) {

        showToastMessage("Add Vehicle Failed. Try again !");

    }
}
