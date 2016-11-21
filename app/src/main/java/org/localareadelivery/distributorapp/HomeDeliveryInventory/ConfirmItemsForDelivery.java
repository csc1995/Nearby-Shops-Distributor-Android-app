package org.localareadelivery.distributorapp.HomeDeliveryInventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.DeliveryGuySelection.AccountsSelectionFragment;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.DeliveryGuySelection.DeliveryGuySelection;
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.ModelRoles.DeliveryGuySelf;
import org.localareadelivery.distributorapp.ModelStatusCodes.OrderStatusHomeDelivery;
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

    DeliveryGuySelf selectedVehicle = null;

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

        Intent intent = new Intent(this, DeliveryGuySelection.class);
        intent.putExtra(AccountsSelectionFragment.INTENT_REQUEST_CODE_KEY, AccountsSelectionFragment.INTENT_CODE_SELECT_DELIVERY_GUY);

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
            vehicleID.setText("ID : " + String.valueOf(selectedVehicle.getDeliveryGuyID()));
            vehicleName.setText(String.valueOf(selectedVehicle.getName()));
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

            order.setDeliveryVehicleSelfID(selectedVehicle.getDeliveryGuyID());
            order.setStatusHomeDelivery(OrderStatusHomeDelivery.PENDING_HANDOVER);

        }


        Call<ResponseBody> call = orderService.putOrderBulk(orderList);

        call.enqueue(this);

    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if(response.code() == 200)
        {
            showToastMessage("Handover Successful !");
            finish();
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
