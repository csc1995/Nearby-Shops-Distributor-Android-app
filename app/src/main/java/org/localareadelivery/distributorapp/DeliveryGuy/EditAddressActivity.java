package org.localareadelivery.distributorapp.DeliveryGuy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.DeliveryGuySelf;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity implements Callback<ResponseBody> {

    DeliveryGuySelf deliveryGuySelf;

    static final String DELIVERY_VEHICLE_SELF_INTENT_KEY = "edit_delivery_vehicle_self_intent_key";


    @Inject
    DeliveryGuySelfService deliveryGuySelfService;


    @Bind(R.id.updateDeliveryVehicle)
    TextView updateDeliveryVehicle;

    // address Fields

    @Bind(R.id.vehicleName)
    EditText vehicleName;


    public EditAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_vehicle_self);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        deliveryGuySelf = getIntent().getParcelableExtra(DELIVERY_VEHICLE_SELF_INTENT_KEY);

        bindDataToViews();

    }




    void getDataFromViews()
    {
        if(deliveryGuySelf !=null)
        {
            deliveryGuySelf.setName(vehicleName.getText().toString());
        }
    }


    void bindDataToViews()
    {
        if(deliveryGuySelf !=null)
        {

            vehicleName.setText(deliveryGuySelf.getName());
        }
    }



    @OnClick(R.id.updateDeliveryVehicle)
    void updateVehicleClick(View view)
    {

        getDataFromViews();

        Call<ResponseBody> call = deliveryGuySelfService.putVehicle(UtilityLogin.getAuthorizationHeaders(this),deliveryGuySelf, deliveryGuySelf.getDeliveryGuyID());
        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if(response.code()==200)
        {
            showToastMessage("Update Successful !");
        }
        else
        {
            showToastMessage("failed to update !");
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

        showToastMessage("Network connection failed !");

    }

    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }



}
