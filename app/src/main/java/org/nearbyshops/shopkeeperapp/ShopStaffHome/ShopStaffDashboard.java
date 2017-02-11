package org.nearbyshops.shopkeeperapp.ShopStaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.nearbyshops.shopkeeperapp.Items.ItemsTypeSimple;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.ItemsByCatSimple;
import org.nearbyshops.shopkeeperapp.ItemsInShop.ItemsInShop;
import org.nearbyshops.shopkeeperapp.ItemsInShopByCat.ItemsInStockByCat;
import org.nearbyshops.shopkeeperapp.Model.Shop;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.OrdersHDMenu.OrdersHomeDelivery;
import org.nearbyshops.shopkeeperapp.PickFromShopMenu.OrdersPickFromShop;
import org.nearbyshops.shopkeeperapp.QuickStockEditor.QuickStockEditor;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopStaffDashboard extends AppCompatActivity {

    @Bind(R.id.border_top) TextView borderTop;
    @Bind(R.id.header_items) TextView headerItems;

    @Bind(R.id.items_by_category) ImageView itemsByCat;
    @Bind(R.id.text_items_by_category) TextView textItemsByCat;

    @Bind(R.id.items) ImageView items;
    @Bind(R.id.text_items)TextView textItems;


    @Bind(R.id.border_items_in_shop) TextView borderItemsInShop;
    @Bind(R.id.header_items_in_shop) TextView headerItemsInShop;

    @Bind(R.id.items_in_shop_by_category) ImageView itemsInShopByCat;
    @Bind(R.id.text_items_in_shop_by_cat) TextView textItemsInShopByCat;

    @Bind(R.id.items_in_shop) ImageView items_in_shop;
    @Bind(R.id.text_items_in_shop) TextView text_items_in_shop;

    @Bind(R.id.quick_stock_editor) ImageView quickStockEditor;
    @Bind(R.id.text_quick_stock_editor_text) TextView textQuickStockEditor;


    @Bind(R.id.border_orders) TextView borderOrders;
    @Bind(R.id.header_orders_and_delivery) TextView headerOrders;
    @Bind(R.id.orders_home_delivery) ImageView home_delivery;
    @Bind(R.id.text_home_delivery_orders) TextView text_home_delivery;

    @Bind(R.id.orders_pick_from_shop) ImageView pickFromShopOrders;
    @Bind(R.id.text_pick_from_shop) TextView textPickFromShop;


    @Bind(R.id.border_bottom) TextView borderBottom;

    ShopStaff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        staff = UtilityLogin.getShopStaff(this);
        setupDashboard();


        Shop shop = new Shop();
        shop.setShopID(staff.getShopID());
        UtilityShopHome.saveShop(shop,this);
    }



    void setupDashboard()
    {
        if(!staff.getEnabled())
        {
            return;
        }

        if(staff.isAddRemoveItemsFromShop() || staff.isAddRemoveItemsFromShop())
        {
            borderTop.setVisibility(View.VISIBLE);
            headerItems.setVisibility(View.VISIBLE);

            itemsByCat.setVisibility(View.VISIBLE);
            textItemsByCat.setVisibility(View.VISIBLE);

            items.setVisibility(View.VISIBLE);
            textItems.setVisibility(View.VISIBLE);
        }


        if(staff.isUpdateStock())
        {
            borderItemsInShop.setVisibility(View.VISIBLE);
            headerItemsInShop.setVisibility(View.VISIBLE);

            itemsInShopByCat.setVisibility(View.VISIBLE);
            textItemsInShopByCat.setVisibility(View.VISIBLE);

            items_in_shop.setVisibility(View.VISIBLE);
            text_items_in_shop.setVisibility(View.VISIBLE);

            quickStockEditor.setVisibility(View.VISIBLE);
            textQuickStockEditor.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);
        }


        if(staff.isCancelOrders()||
                staff.isConfirmOrders()||
                staff.isSetOrdersPacked()||
                staff.isHandoverToDelivery()||
                staff.isMarkOrdersDelivered()||
                staff.isAcceptPaymentsFromDelivery()||
                staff.isAcceptReturns())
        {
            borderOrders.setVisibility(View.VISIBLE);
            headerOrders.setVisibility(View.VISIBLE);
            home_delivery.setVisibility(View.VISIBLE);
            text_home_delivery.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);
        }



        if(staff.isPermitCancelOrdersPFS() ||
                staff.isPermitConfirmOrdersPFS()||
                staff.isPermitSetOrdersPackedPFS()||
                staff.isPermitSetReadyForPickupPFS()||
                staff.isPermitSetPaymentReceivedPFS()||
                staff.isPermitMarkDeliveredPFS())
        {
            borderOrders.setVisibility(View.VISIBLE);
            headerOrders.setVisibility(View.VISIBLE);
            pickFromShopOrders.setVisibility(View.VISIBLE);
            textPickFromShop.setVisibility(View.VISIBLE);
            borderBottom.setVisibility(View.VISIBLE);
        }


    }



    @OnClick(R.id.items)
    void optionItemsClick()
    {
        startActivity(new Intent(this, ItemsTypeSimple.class));
    }


    @OnClick(R.id.items_by_category)
    void optionItemCatApprovals()
    {
        startActivity(new Intent(this, ItemsByCatSimple.class));
    }



    @OnClick(R.id.quick_stock_editor)
    void itemCategoriesClick(View view)
    {
        startActivity(new Intent(this, QuickStockEditor.class));
    }




    @OnClick(R.id.items_in_shop)
    void optionAdminClick(View view)
    {
        startActivity(new Intent(this, ItemsInShop.class));
    }



    @OnClick(R.id.items_in_shop_by_category)
    void distributorAccountClick(View view)
    {
        startActivity(new Intent(this, ItemsInStockByCat.class));
    }


    @OnClick(R.id.orders_home_delivery)
    void homeDeliveryClick()
    {
        startActivity(new Intent(this, OrdersHomeDelivery.class));
    }


    @OnClick(R.id.orders_pick_from_shop)
    void pickFromShopClick()
    {
        startActivity(new Intent(this, OrdersPickFromShop.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
