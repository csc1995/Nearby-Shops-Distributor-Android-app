package org.nearbyshops.shopkeeperapp.OrdersHomeDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import org.nearbyshops.shopkeeperapp.CancelledOrders.CancelledOrdersHomeDelivery;
import org.nearbyshops.shopkeeperapp.DeliveryAccounts.DeliveryGuySelection.AccountsSelectionFragment;
import org.nearbyshops.shopkeeperapp.DeliveryAccounts.DeliveryGuySelection.DeliveryGuySelection;
import org.nearbyshops.shopkeeperapp.HomeDeliveryInventory.HomeDelivery;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.shopkeeperapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersHomeDelivery extends AppCompatActivity {


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


    @OnClick(R.id.home_delivery_inventory)
    void homeDeliveryClick()
    {
        this.startActivity(new Intent(this, HomeDelivery.class));
    }


    @OnClick(R.id.delivery_guy_inventory)
    void deliveryVehiclesClick(View view)
    {
//        Intent intent = new Intent(this, DeliveryVehicleActivity.class);
//        intent.putExtra(DeliveryVehicleActivity.INTENT_REQUEST_CODE_KEY,DeliveryVehicleActivity.INTENT_CODE_DASHBOARD);
//        startActivity(intent);


        Intent intent = new Intent(this, DeliveryGuySelection.class);
        intent.putExtra(AccountsSelectionFragment.INTENT_REQUEST_CODE_KEY,AccountsSelectionFragment.INTENT_CODE_DELIVERY_GUY_INVENTORY);
        startActivity(intent);

    }


    @OnClick(R.id.delivery_guy_dashboard)
    void vehicleDriverDashboardClick(View view)
    {
//        Intent intent = new Intent(this, DeliveryVehicleActivity.class);
//        intent.putExtra(DeliveryVehicleActivity.INTENT_REQUEST_CODE_KEY,DeliveryVehicleActivity.INTENT_CODE_VEHICLE_DRIVER_DASHBOARD);
//        startActivity(intent);

        Intent intent = new Intent(this, DeliveryGuySelection.class);
        intent.putExtra(AccountsSelectionFragment.INTENT_REQUEST_CODE_KEY,AccountsSelectionFragment.INTENT_CODE_DELIVERY_GUY_DASHBOARD);
        startActivity(intent);
    }


    @OnClick(R.id.cancelled_orders)
    void cancelledOrdersClick(View view)
    {
        Intent intent = new Intent(this, CancelledOrdersHomeDelivery.class);
        startActivity(intent);
    }


    @OnClick(R.id.orders_history)
    void orderHistory()
    {
        startActivity(new Intent(this, OrderHistoryHD.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
