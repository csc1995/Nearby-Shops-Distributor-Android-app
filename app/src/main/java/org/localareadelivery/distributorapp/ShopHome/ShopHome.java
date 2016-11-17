package org.localareadelivery.distributorapp.ShopHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import org.localareadelivery.distributorapp.EditStock.EditStock;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategoriesTabs;
import org.localareadelivery.distributorapp.OrdersHome.OrdersHome;
import org.localareadelivery.distributorapp.QuickStockEditor.QuickStockEditor;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.SSEExample.SSEExample;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopHome extends AppCompatActivity implements View.OnClickListener {

    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";

    RelativeLayout addRemoveItemsButton,addEditStockButton;

    RelativeLayout optionEditStock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Toast.makeText(this,String.valueOf(getIntent().getIntExtra(SHOP_ID_INTENT_KEY,0)),Toast.LENGTH_SHORT).show();


        addRemoveItemsButton = (RelativeLayout) findViewById(R.id.option_add_items);
        addRemoveItemsButton.setOnClickListener(this);

//        addEditStockButton = (RelativeLayout) findViewById(R.id.option_edit_stock);
//        addEditStockButton.setOnClickListener(this);


    }


    @OnClick(R.id.option_orders)
    void ordersClick()
    {
        startActivity(new Intent(this, OrdersHome.class));
    }


    @OnClick(R.id.shop_home_quick_stock_editor)
    void quickStockEditorClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));

    }


    @OnClick(R.id.option_edit_stock)
    void editStockClick(View view)
    {
        startActivity(new Intent(this, EditStock.class));
    }

    @OnClick(R.id.option_billing)
    void billingClick(View view)
    {
        startActivity(new Intent(this, SSEExample.class));
    }



    @Override
    public void onClick(View v) {

        switch(v.getId())
        {

            case R.id.option_add_items:

//                startActivity(new Intent(this, ItemCategories.class));

                startActivity(new Intent(this, ItemCategoriesTabs.class));

                break;

            default:
                break;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
