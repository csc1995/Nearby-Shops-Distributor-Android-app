package org.localareadelivery.distributorapp.HomeShopAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdmin;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditShop.EditShop;
import org.localareadelivery.distributorapp.HomeShopAdmin.EditShop.EditShopFragment;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelRoles.ShopAdmin;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopAdminService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopService;
import org.localareadelivery.distributorapp.ShopHome.ShopHome;
import org.localareadelivery.distributorapp.ShopHome.UtilityShopHome;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopAdminHome extends AppCompatActivity {


    @Inject
    ShopAdminService shopAdminService;

    @Inject
    ShopService shopService;

    @Bind(R.id.notice)
    TextView notice;


    public ShopAdminHome() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

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

        checkAccountActivation();
    }


    void checkAccountActivation()
    {
        // if account is deactivated notify User

        ShopAdmin shopAdmin = UtilityLogin.getShopAdmin(this);

        if(shopAdmin!=null && !shopAdmin.getEnabled())
        {
            notice.setVisibility(View.VISIBLE);
        }
        else
        {
            notice.setVisibility(View.GONE);
        }

    }



    @OnClick(R.id.image_edit_profile)
    void editProfileClick()
    {
//        intent.putExtra(EditShopFragment.SHOP_ADMIN_INTENT_KEY,shopAdmin);
//        ShopAdmin shopAdmin = UtilityLogin.getShopAdmin(this);

        Intent intent = new Intent(this, EditShopAdmin.class);
        intent.putExtra(EditShopAdminFragment.EDIT_MODE_INTENT_KEY,EditShopAdminFragment.MODE_UPDATE);
        startActivity(intent);
    }



    @OnClick(R.id.image_edit_shop)
    void editSHopClick()
    {
        if(UtilityShopHome.getShop(this)==null)
        {
            // check online for shop exist or not
            // if shop exist save it in shop home and open it in edit mode
            // if shop does not exist then open edit shop fragment in ADD mode

            /*if(UtilityLogin.getShopAdmin(this)==null)
            {
                return;
            }


            int id = UtilityLogin.getShopAdmin(this).getShopAdminID();*/

            Call<Shop> call = shopService.getShopForShopAdmin(
                    UtilityLogin.getAuthorizationHeaders(this)
            );


            call.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {

                    if(response.code()==200)
                    {
                        UtilityShopHome.saveShop(response.body(),ShopAdminHome.this);

                        // Open Edit fragment in edit mode
                        Intent intent = new Intent(ShopAdminHome.this, EditShop.class);
                        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE);
                        startActivity(intent);

                    }
                    else if(response.code()==204)
                    {
                        Intent intent = new Intent(ShopAdminHome.this, EditShop.class);
                        intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_ADD);
                        startActivity(intent);
                    }
                    else if(response.code()==401||response.code()==403)
                    {
                        Toast.makeText(ShopAdminHome.this,"Not Permitted !",Toast.LENGTH_SHORT)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {




                }
            });


        }
        else
        {
            //     open edit shop in edit mode
            Intent intent = new Intent(ShopAdminHome.this, EditShop.class);
            intent.putExtra(EditShopFragment.EDIT_MODE_INTENT_KEY, EditShopFragment.MODE_UPDATE);
            startActivity(intent);
        }


    }


    @OnClick(R.id.image_shop_dashboard)
    void shopDashboardClick()
    {
        if(UtilityShopHome.getShop(this)==null)
        {
            Call<Shop> call = shopService.getShopForShopAdmin(
                    UtilityLogin.getAuthorizationHeaders(this)
            );


            call.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {

                    if(response.code()==200)
                    {
                        UtilityShopHome.saveShop(response.body(),ShopAdminHome.this);

                        Intent intent = new Intent(ShopAdminHome.this,ShopHome.class);
                        startActivity(intent);

                    }
                    else if(response.code()==204)
                    {
                        Toast.makeText(ShopAdminHome.this,"You have not created Shop yet",Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(response.code()==401||response.code()==403)
                    {
                        Toast.makeText(ShopAdminHome.this,"Not Permitted. Your account is not activated !",Toast.LENGTH_SHORT)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {




                }
            });
        }
        else
        {
            Intent intent = new Intent(this,ShopHome.class);
            startActivity(intent);

        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
