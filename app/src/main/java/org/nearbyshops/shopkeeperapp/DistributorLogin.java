package org.nearbyshops.shopkeeperapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.DeliveryGuyHome;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.EditProfile.EditShopAdmin;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.ShopAdminHome;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopAdmin;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DistributorService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopAdminService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopStaffService;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.ShopStaffHome.ShopStaffHome;
import org.nearbyshops.shopkeeperapp.zDeprecatedCode.ShopList.ShopList;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistributorLogin extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.serviceURLEditText)
    EditText serviceUrlEditText;

//    @Bind(R.id.distributorIDEdittext)
//    EditText distributorIDEditText;

    @Bind(R.id.loginButton)
    Button loginButton;

    @Bind(R.id.signUpButton)
    Button signUpButton;


    @Bind(R.id.username)
    EditText username;

    @Bind(R.id.distributorPassword)
    EditText password;


    @Bind(R.id.role_distributor)
    TextView roleDistributor;

    @Bind(R.id.role_staff)
    TextView roleStaff;

    @Bind(R.id.role_delivery)
    TextView roleDelivery;

    @Inject
    DistributorService service;

    @Inject
    ShopAdminService shopAdminService;

    @Inject
    ShopStaffService shopStaffService;

    @Inject
    DeliveryGuySelfService deliveryRetrofitService;


    public DistributorLogin() {

        DaggerComponentBuilder
                .getInstance()
                .getNetComponent()
                .Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(getApplicationContext()));
        username.setText(UtilityLogin.getUsername(this));
        password.setText(UtilityLogin.getPassword(this));
        setRoleButtons();


        serviceUrlEditText.setText(getServiceURL());

        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                UtilityGeneral.saveServiceURL(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                UtilityGeneral.saveServiceURL(s.toString());

            }
        });



    }



    void clearShopHome()
    {
        UtilityShopHome.saveShop(null,this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearShopHome();
    }



    public String  getServiceURL()
    {
        // Get a handle to the shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginButton:

                startActivity(new Intent(this,ShopList.class));

                break;

        }

    }

/*
    @OnClick(R.id.loginButton)
    public void login_()
    {

//        startActivity(new Intent(this,ShopList.class));
    }*/


    @OnClick(R.id.signUpButton)
    void signUpClick()
    {
        Intent intent = new Intent(this, EditShopAdmin.class);
        intent.putExtra(EditShopAdminFragment.EDIT_MODE_INTENT_KEY,EditShopAdminFragment.MODE_ADD);
        startActivity(intent);
    }


    @OnClick(R.id.loginButton)
    public void login() {


        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if(username.length()==0 || password.length()==0)
        {
            showSnackBar("Please enter username and password");
            return;
        }

        int role = UtilityLogin.getRoleID(this);

        if (role == UtilityLogin.ROLE_DISTRIBUTOR) {

            networkCallLoginShopAdmin();


        } else if (role == UtilityLogin.ROLE_STAFF) {

            networkCallLoginStaff();

        } else if (role == UtilityLogin.ROLE_DELIVERY)
        {
            networkCallDeliveryLogin();
        }
        else if(role == -1)
        {
            showSnackBar("Please select a Role !");
        }

    }



    private void networkCallLoginShopAdmin()
    {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        Call<ShopAdmin> call = shopAdminService.getShopAdminLogin(UtilityLogin.baseEncoding(username,password));

        call.enqueue(new Callback<ShopAdmin>() {
            @Override
            public void onResponse(Call<ShopAdmin> call, Response<ShopAdmin> response) {

                if(response.body()!=null && response.code() ==200)
                {
                    UtilityLogin.saveCredentials(DistributorLogin.this,
                            DistributorLogin.this.username.getText().toString(),
                            DistributorLogin.this.password.getText().toString());

                    UtilityLogin.saveShopAdmin(response.body(),DistributorLogin.this);
                    startActivity(new Intent(DistributorLogin.this,ShopAdminHome.class));

                }
                else if(response.code() ==401)
                {
                    showSnackBar("We are not able to identify you !");
                }
                else if(response.code()==403)
                {
                    showSnackBar("Your account is disabled. Please contact administrator for more information !");
                }
                else
                {
                    showSnackBar("Server Error !");
                }

            }

            @Override
            public void onFailure(Call<ShopAdmin> call, Throwable t) {
                showSnackBar("No Internet. Please Check your Internet Connection !");
            }
        });
    }




    private void networkCallLoginStaff() {


        String username = this.username.getText().toString();
        String password = this.password.getText().toString();


        Call<ShopStaff> call = shopStaffService.getLogin(
                UtilityLogin.baseEncoding(username,password)
        );


        call.enqueue(new Callback<ShopStaff>() {
            @Override
            public void onResponse(Call<ShopStaff> call, Response<ShopStaff> response) {

                if(response.body()!=null && response.code() ==200)
                {

                    UtilityLogin.saveCredentials(DistributorLogin.this,
                            DistributorLogin.this.username.getText().toString(),
                            DistributorLogin.this.password.getText().toString());

                    UtilityLogin.saveShopStaff(response.body(),DistributorLogin.this);
                    startActivity(new Intent(DistributorLogin.this,ShopStaffHome.class));

                }
                else if(response.code() ==401)
                {
                    showSnackBar("We are not able to identify you !");
                }
                else if(response.code()==403)
                {
                    showSnackBar("Your account is disabled. Please contact administrator for more information !");
                }
                else
                {
                    showSnackBar("Server Error ! Code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ShopStaff> call, Throwable t) {

                showSnackBar("Network Failed. Please Check your Internet Connection !");

            }
        });




    }


    private void networkCallDeliveryLogin()
    {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        Call<DeliveryGuySelf> call = deliveryRetrofitService
                .getLogin(UtilityLogin.baseEncoding(username,password));

        call.enqueue(new Callback<DeliveryGuySelf>() {
            @Override
            public void onResponse(Call<DeliveryGuySelf> call, Response<DeliveryGuySelf> response) {

                if(response.body()!=null && response.code() ==200)
                {

                    UtilityLogin.saveCredentials(DistributorLogin.this,
                            DistributorLogin.this.username.getText().toString(),
                            DistributorLogin.this.password.getText().toString());

                    UtilityLogin.saveDeliveryGuySelf(response.body(),DistributorLogin.this);
                    startActivity(new Intent(DistributorLogin.this,DeliveryGuyHome.class));

                }
                else if(response.code() ==401)
                {
                    showSnackBar("We are not able to identify you !");
                }
                else if(response.code()==403)
                {
                    showSnackBar("Your account is disabled. Please contact administrator for more information !");
                }
                else
                {
                    showSnackBar("Server Error !");
                }
            }

            @Override
            public void onFailure(Call<DeliveryGuySelf> call, Throwable t) {

                showSnackBar("No Internet. Please Check your Internet Connection !");
            }
        });

    }


    void showSnackBar(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



//    private void networkCallLoginDistributor() {
//
//
//        String username = this.username.getText().toString();
//        String password = this.password.getText().toString();

//        if(username.length()==0 || password.length()==0)
//        {
//            showSnackBar("Please enter password and Username");
//            return;
//        }
//
//
//        setProgressBar(true);


//        Call<Distributor> call = service.loginDistributor(UtilityLogin.baseEncoding(username,password));
//
//        call.enqueue(new Callback<Distributor>() {
//            @Override
//            public void onResponse(Call<Distributor> call, Response<Distributor> response) {
//
//
//                if(response.body()!=null && response.code()==200)
//                {
////                    Gson gson = new Gson();
////                    Log.d("login", gson.toJson(admin));
//
//                    UtilityLogin.saveCredentials(DistributorLogin.this,
//                            DistributorLogin.this.username.getText().toString(),
//                            DistributorLogin.this.password.getText().toString());
//
//                    UtilityLogin.saveDistributor(response.body(),DistributorLogin.this);
//
//                    startActivity(new Intent(DistributorLogin.this,DistributorHome.class));
//
//                }
//
//                if(response.code()==200)
//                {
//
//                }
//                else if(response.code()==403 || response.code() ==401)
//                {
//                    showSnackBar("Unable to login. Username or password is incorrect !");
//                }
//                else
//                {
//                    showSnackBar("Server Error !");
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Distributor> call, Throwable t) {
//
//                    showSnackBar("No Internet. Please Check your Internet Connection !");
//
//            }
//        });
//
//
//    }


    @OnClick(R.id.role_distributor)
    void selectRoleDistributor()
    {
        clearSelection();
        roleDistributor.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleDistributor.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_DISTRIBUTOR);
    }


    @OnClick(R.id.role_staff)
    void selectRoleStaff()
    {
        clearSelection();
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_STAFF);
    }



    @OnClick(R.id.role_delivery)
    void selectRoleDelivery()
    {
        clearSelection();
        roleDelivery.setTextColor(ContextCompat.getColor(this,R.color.white));
        roleDelivery.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColorDark));

        UtilityLogin.setRoleID(this,UtilityLogin.ROLE_DELIVERY);
    }



    void clearSelection()
    {
        roleDistributor.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));
        roleStaff.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));
        roleDelivery.setTextColor(ContextCompat.getColor(this,R.color.blueGrey800));

        roleDistributor.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
        roleStaff.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
        roleDelivery.setBackgroundColor(ContextCompat.getColor(this,R.color.light_grey));
    }




    void setRoleButtons() {

        if (UtilityLogin.getRoleID(this) == UtilityLogin.ROLE_DISTRIBUTOR)
        {
            selectRoleDistributor();
        }
        else if(UtilityLogin.getRoleID(this)== UtilityLogin.ROLE_STAFF)
        {
            selectRoleStaff();
        }
        else if(UtilityLogin.getRoleID(this)==UtilityLogin.ROLE_DELIVERY)
        {
            selectRoleDelivery();
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
