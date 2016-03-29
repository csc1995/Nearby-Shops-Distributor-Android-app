package org.localareadelivery.distributorapp.ShopList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;

import org.localareadelivery.distributorapp.Model.Image;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ImageService;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddShop extends AppCompatActivity {


    @Bind(R.id.shopName) EditText shopName;
    @Bind(R.id.radiusOfService) EditText radiusOfService;
    @Bind(R.id.latitude) EditText latitude;
    @Bind(R.id.longitude) EditText longitude;
    @Bind(R.id.averageRating) EditText averageRating;
    @Bind(R.id.deliveryCharges) EditText deliveryCharges;
    @Bind(R.id.distributorID) EditText distributorID;

    @Bind(R.id.addShopButton) Button addShop;

    @Bind(R.id.uploadShopImage) Button uploadShopImage;

    @Bind(R.id.result)TextView result;

    @Bind(R.id.uploadImage)
    ImageView resultView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_shop);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        distributorID.setText(String.valueOf(getDistributorID()));
    }


    void makeRequest(Shop shop)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopService shopService = retrofit.create(ShopService.class);

        Call<Shop> shopCall = shopService.insertShop(shop);


        shopCall.enqueue(new Callback<Shop>() {

            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                Shop shopResponse = response.body();

                displayResult(shopResponse);
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });


    }



    void displayResult(Shop shop)
    {
        String resultString = "ID : " + shop.getShopID()
                        +"\n : " + shop.getShopName()
                        +"\n : " + String.valueOf(shop.getLatitude());



        result.setText(resultString);
    }




    @OnClick(R.id.addShopButton)
    void addShop()
    {



        Shop shop = new Shop();

        shop.setDistributorID(getDistributorID());
        shop.setShopName(shopName.getText().toString());

        try {

            shop.setAverageRating(Double.parseDouble(averageRating.getText().toString()));
            shop.setDeliveryCharges(Double.parseDouble(averageRating.getText().toString()));
            shop.setLatitude(Double.parseDouble(latitude.getText().toString()));
            shop.setLongitude(Double.parseDouble(longitude.getText().toString()));
            shop.setRadiusOfService(Double.parseDouble(radiusOfService.getText().toString()));


        }catch (Exception ex)
        {

        }

        makeRequest(shop);
    }



    private Uri mDestinationUri;

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

    @OnClick(R.id.uploadShopImage)
    void uploadShopImage()
    {


        mDestinationUri = Uri.fromFile(new File(getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));


        Log.d("applog", "Cache Dir Path : " + getCacheDir().getPath());

        resultView.setImageDrawable(null);
        //Crop.pickImage(this);

        showFileChooser();

    }


    @Bind(R.id.uploadPickedImage) Button uploadPickedImage;





    public void startCropActivity(Uri sourceUri)
    {

        UCrop.of(sourceUri, mDestinationUri)

                .start(this);


        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }


    /*


            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);






            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";



            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("image/jpeg"), descriptionString);




     */






    Image image = null;

    @OnClick(R.id.uploadPickedImage)
    void uploadPickedImage()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageService imageService = retrofit.create(ImageService.class);

        //ShopService shopService = retrofit.create(ShopService.class);

        //Call<Image> shopCall = imageService.uploadImage();

        Log.d("applog","onClickUploadImage");





        if(imageUri!=null)
        {
            // do upload operations



            Log.d("applog","inside image URI !=null");


            // use the FileUtils to get the actual file by uri
          //File file = File.getFile(this, imageUri);



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
                        1);

                return;

            }



            //String filePath = getFilePath(this,imageUri);


            String filePath2 = getRealPathFromURI(imageUri);

            //String filepath3 = getRealPathFromURI2(imageUri);


            File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");








            //byte [] data = BitmapUtility.getBitmapToBytes(((BitmapDrawable) resultView.getDrawable()).getBitmap());


            //Log.d("applog",imageUri.toString());

            //multipart/form-data
            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), file);

            //imageService.uploadImageMultipart(body)


            //.addPart(Headers.of("Content-Type", "image/png"),
                //RequestBody.create(MediaType.parse("image/png"), file))


            //.addPart(RequestBody.create(MediaType.parse("image/png"), file))

            RequestBody requestBodyMulti = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("sample",file.getName(),requestFile)
                    .build();


            MediaType MEDIA_TYPE_PNG = MediaType.parse("multipart/form-data");

            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_PNG, file);




            // Marker

            RequestBody requestBodyBinary = null;

            InputStream in = null;

            try {
                in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1);

                requestBodyBinary = RequestBody

                    .create(MediaType.parse("application/octet-stream"), buf);



                //Bitmap.createScaledBitmap()


            } catch (Exception e) {
                e.printStackTrace();
            }



            Call<Image> imageCall = imageService.uploadImage(requestBodyBinary);



            imageCall.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {

                    image = response.body();

                    Log.d("applog","inside retrofit call !" + String.valueOf(response.code()));

                }

                @Override
                public void onFailure(Call<Image> call, Throwable t) {

                    Log.d("applog","inside Error: " + t.getMessage());


                }
            });


            if(image!=null) {

                Toast.makeText(this, "inside File Upload : " + image.getPath(), Toast.LENGTH_SHORT).show();

                Log.d("applog","image Path : " + image.getPath());

            }
        }
    }



    //Convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI2(Uri contentUri) {
        String [] proj={MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = managedQuery( contentUri,
                proj,     // Which columns to return
                null,     // WHERE clause; which rows to return (all rows)
                null,     // WHERE clause selection arguments (none)
                null);     // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @SuppressLint("NewApi")
    public String getFilePath(Context context, Uri uri)
    {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }






    Uri imageUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);


        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && result != null && result.getData() != null) {

            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {
                startCropActivity(result.getData());
            }

           }



        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(result);

            imageUri = resultUri;

            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                //Setting the Bitmap to ImageView
                resultView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(result);
        }
    }

    private void beginCrop(Uri source) {

        //imageUri = source;

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(16,9).withMaxSize(300,200).start(this);
    }



    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            imageUri = Crop.getOutput(result);

            resultView.setImageURI(imageUri);

        } else if (resultCode == Crop.RESULT_ERROR) {

            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private int PICK_IMAGE_REQUEST = 21;



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
