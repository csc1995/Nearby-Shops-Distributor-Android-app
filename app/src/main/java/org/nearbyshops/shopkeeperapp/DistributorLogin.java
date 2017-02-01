package org.nearbyshops.shopkeeperapp;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.DeliveryGuyHome;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.EditProfile.EditShopAdmin;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.EditProfile.EditShopAdminFragment;
import org.nearbyshops.shopkeeperapp.HomeShopAdmin.ShopAdminHome;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelRoles.Deprecated.Distributor;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopAdmin;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DistributorService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopAdminService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopStaffService;
import org.nearbyshops.shopkeeperapp.Services.ServicesActivity;
import org.nearbyshops.shopkeeperapp.Services.UtilityLocation;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.ShopStaffHome.ShopStaffHome;
import org.nearbyshops.shopkeeperapp.Utility.UtilityServiceConfig;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DistributorLogin extends AppCompatActivity implements View.OnClickListener {

//    @Bind(R.id.serviceURL) EditText serviceUrlEditText;
    //@Bind(R.id.serviceURLEditText) EditText serviceUrlEditText;

    @Bind(R.id.loginButton) Button loginButton;
    @Bind(R.id.signUpButton) Button signUpButton;
    @Bind(R.id.username) EditText username;
    @Bind(R.id.distributorPassword) EditText password;
    @Bind(R.id.role_distributor) TextView roleDistributor;
    @Bind(R.id.role_staff) TextView roleStaff;
    @Bind(R.id.role_delivery) TextView roleDelivery;

//    @Inject DistributorService service;
//    @Inject ShopAdminService shopAdminService;
//    @Inject ShopStaffService shopStaffService;
//    @Inject DeliveryGuySelfService deliveryRetrofitService;


    @Inject Gson gson;


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


        username.setText(UtilityLogin.getUsername(this));
        password.setText(UtilityLogin.getPassword(this));
        setRoleButtons();


//        serviceUrlEditText.setText(getServiceURL());
//
//        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                UtilityGeneral.saveServiceURL(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                UtilityGeneral.saveServiceURL(s.toString());
//
//            }
//        });


        setupServiceURLEditText();

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

        setProgressBar(true);

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create())

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ShopAdminService adminServiceSimple = retrofit.create(ShopAdminService.class);
        Call<ShopAdmin> call = adminServiceSimple.getShopAdminLogin(UtilityLogin.baseEncoding(username,password));

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
                    showSnackBar("Failed : Code !" + String.valueOf(response.code()));
                }

                setProgressBar(false);
            }

            @Override
            public void onFailure(Call<ShopAdmin> call, Throwable t) {
                showSnackBar("No Internet. Please Check your Internet Connection !");
                setProgressBar(false);
            }
        });
    }




    private void networkCallLoginStaff() {


        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        setProgressBar(true);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ShopStaffService staffServiceLocal = retrofit.create(ShopStaffService.class);

        Call<ShopStaff> call = staffServiceLocal.getLogin(
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

                setProgressBar(false);
            }

            @Override
            public void onFailure(Call<ShopStaff> call, Throwable t) {

                showSnackBar("Network Failed. Please Check your Internet Connection !");

                setProgressBar(false);

            }
        });




    }


    private void networkCallDeliveryLogin()
    {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        setProgressBar(true);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        DeliveryGuySelfService deliveryGuyLocal = retrofit.create(DeliveryGuySelfService.class);


        Call<DeliveryGuySelf> call = deliveryGuyLocal
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

                setProgressBar(false);
            }

            @Override
            public void onFailure(Call<DeliveryGuySelf> call, Throwable t) {

                showSnackBar("No Internet. Please Check your Internet Connection !");

                setProgressBar(false);
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




    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    void setProgressBar(boolean visible)
    {
        progressBar.setIndeterminate(true);

        if(visible)
        {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

    }



    // Setup Status Light

    @Bind(R.id.text_input_service_url) TextInputLayout textInputServiceURL;
    @Bind(R.id.serviceURL) EditText serviceURL;

    UrlValidator urlValidator;

    void setupServiceURLEditText()
    {
        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);

        serviceURL.setText(UtilityGeneral.getServiceURL(this));

        serviceURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (urlValidator.isValid(s.toString())) {
                    UtilityGeneral.saveServiceURL(s.toString());
                    textInputServiceURL.setError(null);
                    textInputServiceURL.setErrorEnabled(false);
                    updateStatusLight();
                }
                else
                {
//                    serviceURL.setError("URL Invalid");
                    textInputServiceURL.setErrorEnabled(true);
                    textInputServiceURL.setError("Invalid URL");

                    UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }

            }
        });


    }


    void updateStatusLight()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(
                UtilityLocation.getLatitude(this),
                UtilityLocation.getLongitude(this)
        );


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {
                        ServiceConfigurationLocal configurationLocal = response.body();
                        UtilityServiceConfig.saveConfiguration(configurationLocal,DistributorLogin.this);


                        if(configurationLocal.getRt_distance()<=configurationLocal.getServiceRange())
                        {
                            UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_GREEN);
                            setStatusLight();
                        }
                        else
                        {
                            UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_YELLOW);
                            setStatusLight();
                        }

                    }
                    else
                    {
                        UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_RED);
                        setStatusLight();
                    }
                }
                else
                {
                    UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                UtilityServiceConfig.saveServiceLightStatus(DistributorLogin.this,STATUS_LIGHT_RED);
                setStatusLight();

            }
        });

    }


    @Bind(R.id.status_indicator_one) TextView statusLight;
    public static final int STATUS_LIGHT_GREEN = 1;
    public static final int STATUS_LIGHT_YELLOW = 2;
    public static final int STATUS_LIGHT_RED = 3;


    void setStatusLight()
    {
        int status = UtilityServiceConfig.getServiceLightStatus(this);

        if(status == STATUS_LIGHT_GREEN)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_1));
        }
        else if(status == STATUS_LIGHT_YELLOW)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_2));
        }
        else if(status == STATUS_LIGHT_RED)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.deepOrange900));
        }
    }



    @OnClick(R.id.paste_url_button)
    void pasteURLClick()
    {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.getPrimaryClip()!=null)
        {
            serviceURL.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }


    @OnClick(R.id.discover_services_button)
    void discoverServices()
    {
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
    }



}
