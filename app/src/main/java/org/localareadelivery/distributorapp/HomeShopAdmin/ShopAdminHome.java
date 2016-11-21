package org.localareadelivery.distributorapp.HomeShopAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDelivery;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDeliveryFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdmin;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.localareadelivery.distributorapp.ModelRoles.ShopAdmin;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopAdminService;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopAdminHome extends AppCompatActivity {


    @Inject
    ShopAdminService shopAdminService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_admin_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }



    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {

        ShopAdmin shopAdmin = UtilityLogin.getShopAdmin(this);

        Intent intent = new Intent(this, EditShopAdmin.class);
//        intent.putExtra(EditShopAdminFragment.SHOP_ADMIN_INTENT_KEY,shopAdmin);
        intent.putExtra(EditShopAdminFragment.EDIT_MODE_INTENT_KEY,EditShopAdminFragment.MODE_UPDATE);
        startActivity(intent);
    }




    @OnClick(R.id.image_edit_shop)
    void editSHopClick()
    {

    }


    @OnClick(R.id.image_shop_dashboard)
    void shopDashboardClick()
    {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
