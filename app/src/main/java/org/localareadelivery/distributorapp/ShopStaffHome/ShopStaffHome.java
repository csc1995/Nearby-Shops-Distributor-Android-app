package org.localareadelivery.distributorapp.ShopStaffHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.ModelRoles.ShopStaff;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopStaffService;
import org.localareadelivery.distributorapp.ShopStaffHome.EditStaffSelf.EditStaffSelf;
import org.localareadelivery.distributorapp.ShopStaffHome.EditStaffSelf.EditStaffSelfFragment;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopStaffHome extends AppCompatActivity {

    @Bind(R.id.notice) TextView notice;
    @Inject ShopStaffService staffService;

    public ShopStaffHome() {
        DaggerComponentBuilder.getInstance().getNetComponent().Inject(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_staff_home);
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


    }


    @Override
    protected void onResume() {
        super.onResume();
        checkAccountActivation();
    }




    void checkAccountActivation()
    {
        // if account is deactivated notify User

        ShopStaff staff = UtilityLogin.getShopStaff(this);

        if(staff!=null && !staff.getEnabled())
        {
            notice.setVisibility(View.VISIBLE);
        }
        else
        {
            notice.setVisibility(View.GONE);
        }

    }



    @OnClick(R.id.edit_profile)
    void editProfileClick()
    {
        // fetch latest ShopStaff from login endpoint
        // if status is 200 ok open an edit form page

        Call<ShopStaff> call = staffService.getLogin(
                UtilityLogin.getAuthorizationHeaders(this)
        );

        call.enqueue(new Callback<ShopStaff>() {
            @Override
            public void onResponse(Call<ShopStaff> call, Response<ShopStaff> response) {

                if(response.code()==200)
                {
                    UtilityLogin.saveShopStaff(response.body(),ShopStaffHome.this);

                    Intent intent = new Intent(ShopStaffHome.this, EditStaffSelf.class);
                    intent.putExtra(EditStaffSelfFragment.EDIT_MODE_INTENT_KEY, EditStaffSelfFragment.MODE_UPDATE);
                    startActivity(intent);
                }
                else
                {
                    showToastMessage("Failed ! Status code : " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ShopStaff> call, Throwable t) {

                showToastMessage("Failed ! Check your internet connection .");
            }
        });
    }



    @OnClick(R.id.dashboard)
    void dashboardClick()
    {

        startActivity(new Intent(this,ShopStaffDashboard.class));

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
