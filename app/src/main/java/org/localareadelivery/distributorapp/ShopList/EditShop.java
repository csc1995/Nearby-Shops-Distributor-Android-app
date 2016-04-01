package org.localareadelivery.distributorapp.ShopList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.localareadelivery.distributorapp.Model.Image;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ImageService;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditShop extends AppCompatActivity implements LocationListener {


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
    @Bind(R.id.result)
    TextView result;

    @Bind(R.id.getCurrentLocationButton)
    Button getCurrentLocation;


    Shop shop = null;




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

            loadImage(shop.getImagePath());

        }

    }




    final String IMAGES_END_POINT_URL = "/api/Images";


    @Bind(R.id.uploadImage) ImageView resultView;

    void loadImage(String imagePath) {


        if(shop != null)
        {
            Picasso.with(this).load(getServiceURL() + IMAGES_END_POINT_URL + imagePath).into(resultView);
        }

    }







    void bindDataToEditText(Shop shop) {

        if (shop != null) {
            shop.setDistributorID(getDistributorID());

            Log.d("applog", shop.toString());
            shopID.setText(String.valueOf(shop.getShopID()));
            shopName.setText(shop.getShopName());
            radiusOfService.setText(String.valueOf(shop.getRadiusOfService()));
            latitude.setText(String.valueOf(shop.getLatitude()));
            longitude.setText(String.valueOf(shop.getLongitude()));
            deliveryCharge.setText(String.valueOf(shop.getDeliveryCharges()));
            distributorID.setText(String.valueOf(shop.getDistributorID()));

        }
    }


    void getDataFromEditText() {

        if(shop!=null) {

            shop.setShopName(shopName.getText().toString());
            shop.setShopID(Integer.parseInt(shopID.getText().toString()));
            shop.setRadiusOfService(Double.parseDouble(radiusOfService.getText().toString()));
            shop.setLatitude(Double.parseDouble(latitude.getText().toString()));
            shop.setLongitude(Double.parseDouble(longitude.getText().toString()));

            shop.setDeliveryCharges(Double.parseDouble(deliveryCharge.getText().toString()));
        }
    }


    void makePUTRequest() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        // store the recently updated data to the shop object

        getDataFromEditText();

        Call<ResponseBody> shopCall = shopService.updateShop(shop, shop.getShopID());

        shopCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                response.body();

                ResponseBody responseBody = response.body();

                Log.d("applog", String.valueOf(response.isSuccessful()) + response.toString());

                displayResult();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.addShopButton)
    void updateShop() {


        if(isImageChanged)
        {
            uploadPickedImage();


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
        }

        else
        {
            makePUTRequest();
        }

    }



    void displayResult() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        Call<Shop> shopCall = shopService.getShop(shop.getShopID());

        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {


                EditShop.this.result.setText(response.body().toString());

            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });

        /*
        String resultMessage = "ID: " + shop.getShopID()
                + "\nSHOP NAME : " + shop.getShopName()
                + "\nRADIUS OF SERVICE : " + shop.getRadiusOfService()
                + "\nLATITUDE : " + shop.getLatitude()
                + "\nLONGITUDE : " + shop.getLongitude()
                + "\nAVERAGE RATING : " + shop.getAverageRating()
                + "\nDELIVERY CHARGES : " + shop.getDeliveryCharges()
                + "\nDISTRIBUTOR ID : " + shop.getDistributorID();
        */
    }


    public int getDistributorID() {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key), 0);

        return distributorID;
    }


    public String getServiceURL() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key), "default");

        return service_url;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }





    final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    LocationManager mlocationManager;

    @OnClick(R.id.getCurrentLocationButton)
    public void requestPermission() {


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

                uploadPickedImage();

                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
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






    // Utility Methods


    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }




    // code for changing / picking image and saving it in the cache folder

    boolean isImageChanged = false;

    String imagePath = "";

    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        showMessageSnackBar("File delete Status : " + String.valueOf(file.delete()));


        imagePath = null;

        resultView.setImageDrawable(null);

        isImageChanged = true;
    }




    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";
    private Uri mDestinationUri;

    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

        mDestinationUri = Uri.fromFile(new File(getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        Log.d("applog", "Cache Dir Path : " + getCacheDir().getPath());

        //resultView.setImageDrawable(null);
        //Crop.pickImage(this);

        showFileChooser();
    }



    private int PICK_IMAGE_REQUEST = 21;

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    public void startCropActivity(Uri sourceUri) {

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);

        options.setToolbarColor(getResources().getColor(R.color.cyan900));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, mDestinationUri)
                .withOptions(options)
                .start(this);

        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData());

            }

        }
    }







    // upload image after being picked up


    Image image = null;


    void uploadPickedImage() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;

        }


        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody

                    .create(MediaType.parse("application/octet-stream"), buf);

            //Bitmap.createScaledBitmap()

        } catch (Exception e) {
            e.printStackTrace();
        }

        final Call<Image> imageCall = imageService.uploadImage(requestBodyBinary);

        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                image = response.body();

                Log.d("applog", "inside retrofit call !" + String.valueOf(response.code()));
                Log.d("applog", "image Path : " + image.getPath());


                //// TODO: 31/3/16
                // check whether load image call is required. or Not

                loadImage(image.getPath());

                imagePath = image.getPath();



                if (response.code() != 201) {
                    showMessageSnackBar("Unable to upload Image. Try changing the image by in the Edit Screen !");

                    result.setText("Unable to upload Image. Try changing the image by in the Edit Screen !");

                }

                shop.setImagePath(imagePath);

                makePUTRequest();
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

                Log.d("applog", "inside Error: " + t.getMessage());

                showMessageSnackBar("Unable to upload Image. Try changing the image by in the Edit Screen !");

                result.setText("Unable to upload Image. Try changing the image by in the Edit Screen !");


                shop.setImagePath(imagePath);

                makePUTRequest();

            }
        });
    }

}
