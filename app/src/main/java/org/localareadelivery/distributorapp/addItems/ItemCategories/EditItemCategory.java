package org.localareadelivery.distributorapp.addItems.ItemCategories;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.localareadelivery.distributorapp.Model.Image;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitServiceContract.ImageService;
import org.localareadelivery.distributorapp.RetrofitServiceContract.ItemCategoryService;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditItemCategory extends AppCompatActivity{


    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 434;


    @Bind(R.id.itemCategoryID) EditText itemCategoryID;

    @Bind(R.id.itemCategoryName) EditText itemCategoryName;

    @Bind(R.id.itemCategoryDescription) EditText itemCategoryDescription;

    @Bind(R.id.updateItemCategory) Button updateItemCategory;

    @Bind(R.id.result) TextView result;

    @Bind(R.id.isLeafNode) CheckBox isLeafNode;

    final String IMAGES_END_POINT_URL = "/api/Images";

    boolean isImageChanged = false;


    public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";
    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";


    ItemCategory itemCategoryForEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_category);

        ButterKnife.bind(this);

        itemCategoryForEdit = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);



        if(itemCategoryForEdit!=null) {

            bindDataToEditText();

            loadImage(itemCategoryForEdit.getImagePath());
        }



        if(savedInstanceState==null) {
            // delete previous file in the cache - This will prevent accidently uploading the previous image
            File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");

            //showMessageSnackBar("File delete Status : " + String.valueOf(file.delete()));

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    void loadImage(String imagePath) {

        Picasso.with(this).load(getServiceURL() + IMAGES_END_POINT_URL + imagePath).into(resultView);
    }



    void bindDataToEditText()
    {
        if(itemCategoryForEdit!=null) {

            itemCategoryID.setText(String.valueOf(itemCategoryForEdit.getItemCategoryID()));
            itemCategoryName.setText(itemCategoryForEdit.getCategoryName());
            itemCategoryDescription.setText(itemCategoryForEdit.getCategoryDescription());

            isLeafNode.setChecked(itemCategoryForEdit.getIsLeafNode());
        }

    }


    void getDataFromEditText()
    {
        if(itemCategoryForEdit!=null)
        {

            itemCategoryForEdit.setCategoryName(itemCategoryName.getText().toString());
            itemCategoryForEdit.setCategoryDescription(itemCategoryDescription.getText().toString());

            itemCategoryForEdit.setIsLeafNode(isLeafNode.isChecked());
        }

    }




    public void retrofitPUTRequest()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);


        getDataFromEditText();

        Call<ResponseBody> itemCategoryCall = itemCategoryService.updateItemCategory(itemCategoryForEdit,itemCategoryForEdit.getItemCategoryID());


        itemCategoryCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                if (response.code() == 200)
                {
                    Toast.makeText(EditItemCategory.this,"Update Successful !",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });

    }





    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }



    @OnClick(R.id.updateItemCategory)
    public void UpdateItemCategory()
    {

        if(isImageChanged)
        {
             uploadPickedImage();

            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
             isImageChanged = false;

        }else {

            retrofitPUTRequest();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }














    /*
        Utility Methods
     */



    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.uploadImage)
    ImageView resultView;


    void showMessageSnackBar(String message) {

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();

    }


    /*

        // Code for Changeing / picking image and saving it in the cache folder


     */


    // code for changing / picking image and saving it in the cache folder


    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";
    private Uri mDestinationUri;

    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        showMessageSnackBar("File delete Status : " + String.valueOf(file.delete()));


        imagePath = "";
        resultView.setImageDrawable(null);


        isImageChanged = true;
    }


    @OnClick(R.id.textChangePicture)
    void pickShopImage() {
        mDestinationUri = Uri.fromFile(new File(getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        Log.d("applog", "Cache Dir Path : " + getCacheDir().getPath());

        resultView.setImageDrawable(null);
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
//        options.setCompressionQuality(100);

        options.setToolbarColor(getResources().getColor(R.color.cyan900));
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ALL, UCropActivity.SCALE);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, mDestinationUri)
                .withOptions(options)
                .withMaxResultSize(400,300)
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





    /*

    // Code for Uploading Image

     */



    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;

    Image image = null;


    String imagePath = "";

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
            public void onResponse(Call<Image> call, retrofit2.Response<Image> response) {

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

                itemCategoryForEdit.setImagePath(imagePath);

                retrofitPUTRequest();

            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

                Log.d("applog", "inside Error: " + t.getMessage());

                showMessageSnackBar("Unable to upload Image. Try changing the image by in the Edit Screen !");

                result.setText("Unable to upload Image. Try changing the image by in the Edit Screen !");

                itemCategoryForEdit.setImagePath(imagePath);

                retrofitPUTRequest();

            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                uploadPickedImage();

                break;
        }
    }

}
