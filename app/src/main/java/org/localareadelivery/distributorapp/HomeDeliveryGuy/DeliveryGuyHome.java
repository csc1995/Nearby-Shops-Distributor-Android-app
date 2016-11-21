package org.localareadelivery.distributorapp.HomeDeliveryGuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDelivery;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDeliveryFragment;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.DeliveryGuyDashboard;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryGuyHome extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_guy_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @OnClick(R.id.image_dashboard)
    void dashboardClick()
    {
        Intent vehicleDashboardIntent = new Intent(this,DeliveryGuyDashboard.class);
        vehicleDashboardIntent.putExtra(DeliveryGuyDashboard.DELIVERY_VEHICLE_INTENT_KEY, UtilityLogin.getDeliveryGuySelf(this));
        startActivity(vehicleDashboardIntent);
    }

    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(this, EditDelivery.class);
        intent.putExtra(EditDeliveryFragment.DELIVERY_GUY_INTENT_KEY,UtilityLogin.getDeliveryGuySelf(this));
        intent.putExtra(EditDeliveryFragment.EDIT_MODE_INTENT_KEY,EditDeliveryFragment.MODE_UPDATE);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
