package org.localareadelivery.distributorapp.ShopHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.localareadelivery.distributorapp.DeliveryAccounts.DeliveryGuyAccounts;
import org.localareadelivery.distributorapp.Items.ItemsTypeSimple;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.ItemsByCatSimple;
import org.localareadelivery.distributorapp.ItemsInShop.ItemsInStock;
import org.localareadelivery.distributorapp.ItemsInShopByCat.ItemsInStockByCat;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.Notifications.SSEIntentService;
import org.localareadelivery.distributorapp.OrdersHomeDelivery.OrdersHomeDelivery;
import org.localareadelivery.distributorapp.OrdersPickFromShop.OrdersPickFromShop;
import org.localareadelivery.distributorapp.QuickStockEditor.QuickStockEditor;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.SSEExample.SSEExample;
import org.localareadelivery.distributorapp.ShopStaffAccounts.ShopStaffAccounts;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopHome extends AppCompatActivity {

    public static final String SHOP_ID_INTENT_KEY = "shop_id_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupNotifications();

        //Toast.makeText(this,String.valueOf(getIntent().getIntExtra(SHOP_ID_INTENT_KEY,0)),Toast.LENGTH_SHORT).show();


//        addRemoveItemsButton = (RelativeLayout) findViewById(R.id.option_add_items);
//        addRemoveItemsButton.setOnClickListener(this);

//        addEditStockButton = (RelativeLayout) findViewById(R.id.option_edit_stock);
//        addEditStockButton.setOnClickListener(this);
    }


    @OnClick(R.id.option_orders)
    void ordersClick()
    {
        startActivity(new Intent(this, OrdersHomeDelivery.class));
    }


    @OnClick(R.id.option_orders_pick_from_shop)
    void ordersPickFromShop()
    {
        startActivity(new Intent(this, OrdersPickFromShop.class));
    }


    @OnClick(R.id.shop_home_quick_stock_editor)
    void quickStockEditorClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));

    }


    @OnClick(R.id.option_edit_stock)
    void editStockClick(View view)
    {
//        startActivity(new Intent(this, EditStock.class));
        startActivity(new Intent(this, ItemsInStockByCat.class));
    }

    @OnClick(R.id.option_billing)
    void billingClick(View view)
    {
        startActivity(new Intent(this, SSEExample.class));
    }


    @OnClick(R.id.option_delivery_guy_accounts)
    void DeliveryAccountsClick(View view)
    {
        startActivity(new Intent(this, DeliveryGuyAccounts.class));
    }


    @OnClick(R.id.option_items)
    void optionItems()
    {
        startActivity(new Intent(this, ItemsTypeSimple.class));
    }



    @OnClick(R.id.option_add_items)
    void optionItemsByCategory()
    {
        startActivity(new Intent(this, ItemsByCatSimple.class));
    }



    @OnClick(R.id.option_items_in_stock)
    void optionItemsInStock()
    {
        startActivity(new Intent(this, ItemsInStock.class));
    }



    @OnClick(R.id.option_staff_accounts)
    void optionStaffAccounts()
    {
        startActivity(new Intent(this, ShopStaffAccounts.class));
    }





/*

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

*/


    void setupNotifications()
    {
        Shop shop = UtilityShopHome.getShop(this);

        if(shop!=null)
        {
            Intent intent = new Intent(this, SSEIntentService.class);
            intent.putExtra(SSEIntentService.SHOP_ID, shop.getShopID());
            startService(intent);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
