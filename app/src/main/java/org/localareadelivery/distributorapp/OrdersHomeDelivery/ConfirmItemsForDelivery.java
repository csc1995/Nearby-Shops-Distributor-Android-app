package org.localareadelivery.distributorapp.OrdersHomeDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.DeliveryVehicleSelf.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.ModelStats.DeliveryVehicleSelf;
import org.localareadelivery.distributorapp.ModelStats.OrderStatusHomeDelivery;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.OrderService;


import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmItemsForDelivery extends AppCompatActivity implements Callback<ResponseBody> {





    @Bind(R.id.selectDeliveryVehicle)
    TextView selectDeliveryVehicle;

    DeliveryVehicleSelf selectedVehicle = null;

    @Bind(R.id.vehicleName)
    TextView vehicleName;

    @Bind(R.id.vehicleID)
    TextView vehicleID;

    @Bind(R.id.selectedVehicle)
    RelativeLayout selectedVehicleLayout;


    @Bind(R.id.handoverToDeliveryVehicle)
    TextView handoverToDeliveryVehicle;



    @Inject
    OrderService orderService;


    public ConfirmItemsForDelivery() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_items_for_delivery);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }





    @OnClick(R.id.selectDeliveryVehicle)
    void onClickSelectVehicle(View view)
    {

        Intent intent = new Intent(this, DeliveryVehicleActivity.class);
        intent.putExtra(DeliveryVehicleActivity.INTENT_REQUEST_CODE_KEY,DeliveryVehicleActivity.INTENT_CODE_SELECT_VEHICLE);

        startActivityForResult(intent,1);

    }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1 && resultCode == 2 && data != null)
            {
                selectedVehicle = data.getParcelableExtra("output");

                if(selectedVehicle!=null)
                {

                    selectedVehicleLayout.setVisibility(View.VISIBLE);

                    bindVehicleToViews();

                }

            }

        }





    void bindVehicleToViews()
    {
        if(selectedVehicle!=null)
        {
            vehicleID.setText("Vehicle ID : " + String.valueOf(selectedVehicle.getID()));
            vehicleName.setText("Vehicle Name" + String.valueOf(selectedVehicle.getVehicleName()));
        }
    }




    @OnClick(R.id.handoverToDeliveryVehicle)
    void clickHandoverToVehicle(View view)
    {

        if(selectedVehicle==null)
        {

            showToastMessage("Please Select Vehicle !");

            return;
        }

        List<Order> orderList = ApplicationState.getInstance().getSelectedOrdersForDelivery();

        for(Order order: orderList)
        {
            Log.d("datalog","Order ID" + String.valueOf(order.getOrderID()));

            order.setDeliveryVehicleSelfID(selectedVehicle.getID());
            order.setStatusHomeDelivery(OrderStatusHomeDelivery.HANDED_TO_DELIVERY_VEHICLE);

        }


        Call<ResponseBody> call = orderService.putOrderBulk(orderList);

        call.enqueue(this);

    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if(response.code() == 200)
        {
            showToastMessage("Handover Successful !");
        }
        else
        {
            showToastMessage("Not Updated");
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

        showToastMessage("Network Request failed. Try again !");
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
