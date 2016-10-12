package org.localareadelivery.distributorapp.OrdersHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import org.localareadelivery.distributorapp.DeliveryGuy.DeliveryVehicleActivity;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.HomeDelivery;
import org.localareadelivery.distributorapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersHome extends AppCompatActivity {


    RelativeLayout ordersHomeDelivery;
    RelativeLayout orderDeliveryVehicles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick(R.id.order_home_delivery)
    void homeDeliveryClick()
    {
        this.startActivity(new Intent(this, HomeDelivery.class));
    }


    @OnClick(R.id.order_edit_delivery_vehicles)
    void deliveryVehiclesClick(View view)
    {
        Intent intent = new Intent(this, DeliveryVehicleActivity.class);
        intent.putExtra(DeliveryVehicleActivity.INTENT_REQUEST_CODE_KEY,DeliveryVehicleActivity.INTENT_CODE_DASHBOARD);
        startActivity(intent);
    }


    @OnClick(R.id.orderhome_vehicle_driver_dashboard)
    void vehicleDriverDashboardClick(View view)
    {
        Intent intent = new Intent(this, DeliveryVehicleActivity.class);
        intent.putExtra(DeliveryVehicleActivity.INTENT_REQUEST_CODE_KEY,DeliveryVehicleActivity.INTENT_CODE_VEHICLE_DRIVER_DASHBOARD);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
