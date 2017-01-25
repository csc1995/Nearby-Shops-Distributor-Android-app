package org.localareadelivery.distributorapp.OrdersPickFromShop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.localareadelivery.distributorapp.OrderHistoryPFS.OrderHistoryPFS;
import org.localareadelivery.distributorapp.PickFromShopInventory.PickFromShopInventory;
import org.localareadelivery.distributorapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersPickFromShop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_pick_from_shop);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @OnClick(R.id.orders_history)
    void orderHistoryClick()
    {
        startActivity(new Intent(this,OrderHistoryPFS.class));
    }





    @OnClick(R.id.pick_from_shop_inventory)
    void pickFromShopINventoryClick()
    {
        startActivity(new Intent(this, PickFromShopInventory.class));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
