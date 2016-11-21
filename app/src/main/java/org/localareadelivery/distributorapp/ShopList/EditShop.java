package org.localareadelivery.distributorapp.ShopList;

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


import org.localareadelivery.distributorapp.Utility.ImageCalls;
import org.localareadelivery.distributorapp.Utility.ImageCropUtility;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;
import org.localareadelivery.distributorapp.Model.Image;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditShop extends AppCompatActivity implements LocationListener, Callback<Image>{

    // check whether the activity is running or not
    boolean isActivityRunning = false;

    public static final String INTENT_EXTRA_SHOP_KEY = "intentExtraShopKey";
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 2938;

    @Bind(R.id.shopID)
    EditText shopID;

    @Bind(R.id.shopName)
    EditText shopName;

    @Bind(R.id.radiusOfService)
    EditText radiusOfService;

    @Bind(R.id.latitude)
    EditText latitude;

    @Bind(R.id.longitude)
    EditText longitude;

    @Bind(R.id.deliveryCharges)
    EditText deliveryCharge;

    @Bind(R.id.distributorID)
    EditText distributorID;

    @Bind(R.id.addShopButton)
    Button addShopButton;

    @Bind(R.id.getCurrentLocationButton)
    Button getCurrentLocation;


    Shop shop = null;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;

    //String imagePath = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_shop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {

            shop = getIntent().getExtras().getParcelable(INTENT_EXTRA_SHOP_KEY);
        }


        if(shop !=null) {

            bindDataToEditText(shop);

            loadImage(shop.getLogoImagePath());

        }

    }




    final String IMAGES_END_POINT_URL = "/api/Images";


    @Bind(R.id.uploadImage) ImageView resultView;

    void loadImage(String imagePath) {


        if(shop != null)
        {
            Picasso.with(this)
                    .load(UtilityGeneral.getServiceURL(this) + IMAGES_END_POINT_URL + imagePath)
                    .into(resultView);
        }

    }







    void bindDataToEditText(Shop shop) {

        if (shop != null) {
//            shop.setDistributorID(UtilityGeneral.getDistributorID(this));

            Log.d("applog", shop.toString());
            shopID.setText(String.valueOf(shop.getShopID()));
            shopName.setText(shop.getShopName());
            radiusOfService.setText(String.valueOf(shop.getDeliveryRange()));
            latitude.setText(String.valueOf(shop.getLatCenter()));
            longitude.setText(String.valueOf(shop.getLonCenter()));
            deliveryCharge.setText(String.valueOf(shop.getDeliveryCharges()));
//            distributorID.setText(String.valueOf(shop.getDistributorID()));

        }
    }


    void getDataFromEditText(Shop shopForUpdate) {

        if(!isActivityRunning)
        {
            // if activity is not currently running then return

            Toast.makeText(this,"Data binding failed ! Shop not updated. Try again !",Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if(shopForUpdate !=null) {

            shopForUpdate.setShopName(shopName.getText().toString());
            shopForUpdate.setShopID(Integer.parseInt(shopID.getText().toString()));
            shopForUpdate.setDeliveryRange(Double.parseDouble(radiusOfService.getText().toString()));
            shopForUpdate.setLatCenter(Double.parseDouble(latitude.getText().toString()));
            shopForUpdate.setLonCenter(Double.parseDouble(longitude.getText().toString()));

            shopForUpdate.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        }
    }




    @OnClick(R.id.addShopButton)
    void updateShop() {


        if(isImageChanged)
        {

            if (shop !=null) {

                // delete previous Image from the Server
                ImageCalls.getInstance()
                        .deleteImage(
                                shop.getLogoImagePath(),
                                new DeleteImageCallback()
                        );



                if(isImageRemoved)
                {


                    shop.setLogoImagePath("");


                    getDataFromEditText(shop);

                    /*ShopDAO.getInstance()
                            .updateShop(
                                    shop,
                                    new updateShopCallback()
                            );
*/

                }else
                {

                    // Upload the image stored in the cache directory
                    //ImageCropUtility

                    ImageCalls.getInstance()
                            .uploadPickedImage(
                            this,
                            REQUEST_CODE_READ_EXTERNAL_STORAGE,
                            this
                    );

                }
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }

        else
        {
            getDataFromEditText(shop);

            /*ShopDAO.getInstance()
                    .updateShop(
                            shop,
                            new updateShopCallback()
                    );*/
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }





    final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    LocationManager mlocationManager;

    @OnClick(R.id.getCurrentLocationButton)
    public void getCurrentLocation() {


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

                /*
                ImageCalls.getInstance()
                        .uploadPickedImage(
                        this,
                        REQUEST_CODE_READ_EXTERNAL_STORAGE,
                        this);

                        */

                updateShop();

                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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




    class DeleteImageCallback implements Callback<ResponseBody>{

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            Log.i("applog",String.valueOf(response.code()));

            if (response.isSuccessful())
            {
                Toast.makeText(EditShop.this,"Image Removed !",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    }






    // Utility Methods


    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }




    // code for changing / picking image and saving it in the cache folder

    @OnClick(R.id.removePicture)
    void removeImage() {
        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        // Set flags
        isImageChanged = true;
        isImageRemoved = true;
    }




    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

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

                ImageCropUtility.startCropActivity(result.getData(),this);

            }

        } // PICK_IMAGE_REQUEST


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;

        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        } //REQUEST_CROP

    }



    /*

    - Clear Cache - To make sure that previous images are not in the cache

    :: Add Routine

    isImageAdded
        If NO - Simply POST the resource and let the image URL field be blank
        If YES - Upload the Image to the server - Get the Image URL and then POST the resource to the Server

        ::ImageRemovedAfterAdded
           - Reset the isImageAdded flag to NO.


    :: Edit Routine
    isImageChanged (At time of Uploading)
        If No - Do nothing for image URL simply update the attributes and make PUT request on the server
        If Yes
                - Removed (Simply update the image Url Attribute to blank and proceed with PUT resource)
                    - Delete Previous Image From the Server
                - NotRemoved (Upload the image get the image URL and the do the PUT Resource)
                    - Delete Previous Image From the Server
    */


    // Image Upload Callback
    @Override
    public void onResponse(Call<Image> call, Response<Image> response) {

        Image image = null;

        image = response.body();

        Log.d("applog", "inside retrofit call !" + String.valueOf(response.code()));
        Log.d("applog", "image Path : " + image.getPath());



        shop.setLogoImagePath(null);

        if(image!=null)
        {
            shop.setLogoImagePath(image.getPath());
        }

        getDataFromEditText(shop);

        /*ShopDAO.getInstance()
                .updateShop(
                        shop,
                        new updateShopCallback()
                );
*/

    }

    @Override
    public void onFailure(Call<Image> call, Throwable t) {

        Log.d("applog", "inside Error: " + t.getMessage());


        shop.setLogoImagePath("");

        Toast.makeText(this,"Unable to Upload Image. Try Later ! ",Toast.LENGTH_SHORT).show();


        getDataFromEditText(shop);

        /*ShopDAO.getInstance()
                .updateShop(
                        shop,
                        new updateShopCallback()
                );
*/
    }



   /* class updateShopCallback implements ShopDAO.UpdateShopCallback{

        @Override
        public void updateShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

            if(isOffline) {

                if (!isSuccessful){
                    Toast.makeText(EditShop.this,"Application offline ! No Network !",Toast.LENGTH_SHORT).show();
                }

            }else
            {
                if (isSuccessful && httpStatusCode == 200) {

                    Toast.makeText(EditShop.this, "Shop Update Successful !", Toast.LENGTH_SHORT).show();

                }else
                {
                    Toast.makeText(EditShop.this, "Unable to update Shop. Try Again !", Toast.LENGTH_SHORT).show();

                }
            }


        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();

        isActivityRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isActivityRunning = false;
    }
}
