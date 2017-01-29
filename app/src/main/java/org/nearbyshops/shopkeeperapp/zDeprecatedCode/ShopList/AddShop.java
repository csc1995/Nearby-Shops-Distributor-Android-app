package org.nearbyshops.shopkeeperapp.zDeprecatedCode.ShopList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.nearbyshops.shopkeeperapp.Model.Image;
import org.nearbyshops.shopkeeperapp.Model.Shop;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.ImageCalls;
import org.nearbyshops.shopkeeperapp.Utility.ImageCropUtility;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShop extends AppCompatActivity implements LocationListener, Callback<Image>{


    boolean isImageAdded = false;

    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;

//    @Inject ShopDAO shopDAO;


    @Bind(R.id.shopName)
    EditText shopName;

    @Bind(R.id.radiusOfService)
    EditText radiusOfService;

    @Bind(R.id.latitude)
    EditText latitude;

    @Bind(R.id.longitude)
    EditText longitude;

    @Bind(R.id.deliveryCharges)
    EditText deliveryCharges;

    @Bind(R.id.distributorID)
    EditText distributorID;

    @Bind(R.id.addShopButton)
    Button addShop;

    @Bind(R.id.removePicture)
    TextView removePicture;

    @Bind(R.id.result)
    TextView result;

    @Bind(R.id.uploadImage)
    ImageView resultView;

    final String IMAGES_END_POINT_URL = "/api/Images";


    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use dependency injection
//        DaggerComponentBuilder.getInstance()
//                .getDaoComponent()
//                .Inject(this);


        setContentView(R.layout.activity_add_shop);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        distributorID.setText(String.valueOf(UtilityGeneral.getDistributorID(this)));

    }




    void displayResult(Shop shop) {

        String resultString = "ID : " + shop.getShopID()
                + "\n : " + shop.getShopName()
                + "\n : " + String.valueOf(shop.getLatCenter())
                + "\n : " + String.valueOf(shop.getLonCenter())
                + "\n : " + shop.getLogoImagePath();

        result.setText(resultString);
    }




    void addShop(String imagePath) {

        //uploadPickedImage();

        Shop shop = new Shop();
//        shop.setDistributorID(UtilityGeneral.getDistributorID(this));
        shop.setShopName(shopName.getText().toString());
        shop.setLogoImagePath(imagePath);

        try {

            shop.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            shop.setLonCenter(Double.parseDouble(longitude.getText().toString()));
            shop.setDeliveryRange(Double.parseDouble(radiusOfService.getText().toString()));

        } catch (Exception ex) {

        }

//        shopDAO.createShop(shop,this);
    }





    void loadImage(String imagePath) {

        Picasso.with(this).load(UtilityGeneral.getServiceURL(this) + IMAGES_END_POINT_URL + imagePath).into(resultView);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    /*

     Code for adding the location

     */




    @Bind(R.id.getCurrentLocationButton) Button getCurrentLocation;

    final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    LocationManager mlocationManager;


    @OnClick(R.id.getCurrentLocationButton)
    public void requestLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

            return;
        }

        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);

                }
                break;


            case REQUEST_CODE_READ_EXTERNAL_STORAGE:


                addShopButton();

                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        //      mGoogleApiClient.disconnect();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)

        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if(mlocationManager!=null)
        {

            mlocationManager.removeUpdates(this);
        }
    }



    @Override
    public void onLocationChanged(Location location) {

        if(location!=null)
        {

            longitude.setText(String.valueOf(location.getLongitude()));
            latitude.setText(String.valueOf(location.getLatitude()));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }

            mlocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    // Utility Methods


    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();

    }





    // code for changing / picking image and saving it in the cache folder
    /*

    Inputs: Activity Reference and Request Code:

    */



    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        // reset the flag to reflect the status of image addition
        isImageAdded = false;
    }




    //private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";
    //private Uri mDestinationUri;

    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

        //Log.d("applog", "Cache Dir Path : " + getCacheDir().getPath());

        resultView.setImageDrawable(null);

        ImageCropUtility.showFileChooser(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if (requestCode == ImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {
                //startCropActivity(result.getData());

                ImageCropUtility.startCropActivity(result.getData(),this);
            }

        }



        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageAdded = true;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }// Request Crop
    }




    /*

    // Code for Uploading Image

       // Takes the file in the cache directory and upload it to the server
       // Retrives the image path from the response. and call the post Shop method.

     */



    @OnClick(R.id.addShopButton)
    void addShopButton(){

        if(isImageAdded)
        {
            // Upload the image followed the shop data update
            ImageCalls.getInstance().uploadPickedImage(
                    this,
                    REQUEST_CODE_READ_EXTERNAL_STORAGE,
                    this
            );
        }
        else
        {
            // do not upload the image to the server and make a direct update to the server for shop data.
            addShop(null);

        }

    }




    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {

        Image image = null;

        image = response.body();

        Log.d("applog", "inside retrofit call !" + String.valueOf(response.code()));
        Log.d("applog", "image Path : " + image.getPath());


        //// TODO: 31/3/16
        // check whether load image call is required. or Not

        loadImage(image.getPath());



        if (response.code() != 201) {

            result.setText("Unable to upload Image. Try changing the image by in the Edit Screen !");

        }

        addShop(image.getPath());

    }

    @Override
    public void onFailure(Call<Image> call, Throwable t) {

        Log.d("applog", "inside Error: " + t.getMessage());

        Toast.makeText(this,"Unable to Upload Image. Try Later ! ",Toast.LENGTH_SHORT).show();

        result.setText("Unable to upload Image. Try changing the image by in the Edit Screen !");

        addShop(null);
    }





    public void createShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, Shop shop) {

        if (!isOffline) {

            // application online

            if(isSuccessful)
            {
                // successful response

                if (httpStatusCode == 201) {

                    showMessageSnackBar("Shop added Successfully !");

                } else {

                    showMessageSnackBar("Error: Unable to add shop !");
                }

                displayResult(shop);

                // // TODO: 30/3/16
                // Show snack bar in order to display successful or failure of the image upload


            }
            else
            {
                // failure response
                showMessageSnackBar("Error: Unable to add shop !");
            }

        }
        else
        {
            // application offline

            Toast.makeText(this,"Application is offline! Unable to add Shop",Toast.LENGTH_SHORT)
                    .show();
        }


    }



    /*
    - Clear Cache - To make sure that previous images are not in the cache

    :: Add Routine

    isImageAdded
    If NO - Simply POST the resource and let the image URL field be blank
    If YES - Upload the Image to the server - Get the Image URL and then POST the resource to the Server

    ::ImageRemovedAfterAdded
    - Reset the isImageAdded flag to NO.

    */


}