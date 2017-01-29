package org.nearbyshops.shopkeeperapp.DeliveryGuyHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.DeliveryGuyDashboard;
import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.EditProfile.EditDeliverySelf;
import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.EditProfile.EditDeliverySelfFragment;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

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
        vehicleDashboardIntent.putExtra(DeliveryGuyDashboard.DELIVERY_GUY_INTENT_KEY_DASHBOARD, UtilityLogin.getDeliveryGuySelf(this));
        startActivity(vehicleDashboardIntent);
    }

    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {
        Intent intent = new Intent(this, EditDeliverySelf.class);
//        intent.putExtra(EditDeliverySelfFragment.DELIVERY_GUY_INTENT_KEY,UtilityLogin.getDeliveryGuySelf(this));
        intent.putExtra(EditDeliverySelfFragment.EDIT_MODE_INTENT_KEY,EditDeliverySelfFragment.MODE_UPDATE);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
