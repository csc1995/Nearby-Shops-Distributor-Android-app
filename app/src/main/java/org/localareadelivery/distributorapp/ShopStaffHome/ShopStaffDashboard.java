package org.localareadelivery.distributorapp.ShopStaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Items.ItemsTypeSimple;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.ItemsByCatSimple;
import org.localareadelivery.distributorapp.ItemsInShop.ItemsInStock;
import org.localareadelivery.distributorapp.ItemsInShopByCat.ItemsInStockByCat;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelRoles.ShopStaff;
import org.localareadelivery.distributorapp.QuickStockEditor.QuickStockEditor;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ShopHome.UtilityShopHome;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

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
        startActivity(new Intent(this, ItemsInStock.class));
    }



    @OnClick(R.id.items_in_shop_by_category)
    void distributorAccountClick(View view)
    {
        startActivity(new Intent(this, ItemsInStockByCat.class));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
