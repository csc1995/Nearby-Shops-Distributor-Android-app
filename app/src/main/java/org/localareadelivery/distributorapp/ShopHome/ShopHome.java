package org.localareadelivery.distributorapp.ShopHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.addItems.ItemCategories.ItemCategories;

public class ShopHome extends AppCompatActivity implements View.OnClickListener {

    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";

    Button addRemoveItemsButton,addEditStockButton,ordersButton,statisticsButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Toast.makeText(this,String.valueOf(getIntent().getIntExtra(SHOP_ID_INTENT_KEY,0)),Toast.LENGTH_SHORT).show();


        addRemoveItemsButton = (Button) findViewById(R.id.addRemoveItemsButton);
        addRemoveItemsButton.setOnClickListener(this);

        addEditStockButton = (Button) findViewById(R.id.addStockButton);
        addEditStockButton.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {

            case R.id.addRemoveItemsButton:

                startActivity(new Intent(this, ItemCategories.class));



                break;

            case R.id.addStockButton:

                startActivity(new Intent(this, org.localareadelivery.distributorapp.addStock.ItemCategories.class));

                break;

            default:
                break;
        }


    }
}
