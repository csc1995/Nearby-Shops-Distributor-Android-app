package org.nearbyshops.shopkeeperapp;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.shopkeeperapp.DeliveryGuyHome.DeliveryGuyHome;
import org.nearbyshops.shopkeeperapp.ModelServiceConfig.Endpoints.ServiceConfigurationEndPoint;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSDS.ServiceConfigService;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.EditProfile.EditShopAdmin;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.EditProfile.EditShopAdminFragment;
import org.nearbyshops.shopkeeperapp.ShopAdminHome.ShopAdminHome;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopAdmin;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopAdminService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopStaffService;
import org.nearbyshops.shopkeeperapp.Services.ServicesActivity;
import org.nearbyshops.shopkeeperapp.Services.UtilityLocation;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.ShopStaffHome.ShopStaffHome;
import org.nearbyshops.shopkeeperapp.Utility.UtilityServiceConfig;
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

public class DistributorLogin extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {



    // variables for google location api
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    @Inject ServiceConfigService serviceConfigService;
    boolean isDestroyed;

//    boolean isDestroyed;



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


        // Location Code

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Location code ends




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
        setStatusLight();
    }



    void clearShopHome()
    {
        UtilityShopHome.saveShop(null,this);
    }


    @Override
    protected void onStart() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }


    @Override
    protected void onStop() {


        isDestroyed = true;

        if (mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }

        super.onStop();
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


    // location code


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);


            return;
        }


        if (mGoogleApiClient == null) {

            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {

            saveLocation(mLastLocation);


        }else
        {

            // if getlastlocation does not work then request the device to get the current location.
            createLocationRequest();


            if(mLocationRequest!=null)
            {
                startLocationUpdates();
            }

        }

    }


    private static final int REQUEST_CHECK_SETTINGS = 3;


    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    DistributorLogin.this,
                                    REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        // ...
                        break;

                }
            }

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {


                showToastMessage("Permission granted !");

                onConnected(null);

            } else {


                showToastMessage("Permission not granted !");
            }
        }
    }






    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("applog","Google api client connection failed !");

    }


    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);

            return;
        }


        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }



    protected void stopLocationUpdates() {

        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        saveLocation(location);
        stopLocationUpdates();
    }


    void saveLocation(Location location)
    {
        UtilityLocation.saveLatitude((float) location.getLatitude(),this);
        UtilityLocation.saveLongitude((float) location.getLongitude(),this);

        setupURLAuto();
    }





    void setupURLAuto()
    {

        if(!UtilityGeneral.getServiceURL(this).equals(UtilityGeneral.DEFAULT_SERVICE_URL))
        {
            // do not proceed if user has already set the URL
            return;
        }


        String current_sort = "IS_OFFICIAL_SERVICE_PROVIDER desc,distance asc";

        Call<ServiceConfigurationEndPoint> call = serviceConfigService.getShopListSimple(
                UtilityLocation.getLatitude(this),
                UtilityLocation.getLongitude(this),
                null,null,
                null,
                null,null,
                null,
                current_sort,1,0);


        call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
            @Override
            public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!= null)
                {
                    response.body().getItemCount();


                    if(response.body().getResults()!=null && response.body().getResults().size()>=1)
                    {
//                        UtilityGeneral.saveServiceURL(
//                                response.body().getResults().get(0).getServiceURL(),
//                                Home.this
//                        );

                        String serviceURLString = response.body().getResults().get(0).getServiceURL();

                        if (urlValidator.isValid(serviceURLString)) {
                            UtilityGeneral.saveServiceURL(serviceURLString);
                            textInputServiceURL.setError(null);
                            textInputServiceURL.setErrorEnabled(false);
                            serviceURL.setText(serviceURLString);
                            updateStatusLight();
                        }


                    }

                }

            }

            @Override
            public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

//                showToastMessage("Network Request failed !");

            }
        });

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 1:

                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    onConnected(null);

                }
                else
                {
                    showToastMessage("Permission denied cant access location !");
                }


                break;


            case 2:

                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    startLocationUpdates();

                }
                else
                {
                    showToastMessage("Permission denied cant access location !");
                }

            default:

                break;

        }
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }





}
