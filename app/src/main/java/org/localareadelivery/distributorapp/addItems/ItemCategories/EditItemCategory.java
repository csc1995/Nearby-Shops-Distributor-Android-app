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


import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Image;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ImageService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemCategoryService;
import org.localareadelivery.distributorapp.Utility.ImageCalls;
import org.localareadelivery.distributorapp.Utility.ImageCropUtility;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.inject.Inject;

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

public class EditItemCategory extends AppCompatActivity implements Callback<Image> {


    @Inject
    ItemCategoryService itemCategoryService;


    @Bind(R.id.uploadImage)
    ImageView resultView;

    @Bind(R.id.textChangePicture)
    TextView changePicture;

    @Bind(R.id.itemCategoryID) EditText itemCategoryID;

    @Bind(R.id.itemCategoryName) EditText itemCategoryName;

    @Bind(R.id.itemCategoryDescription) EditText itemCategoryDescription;

    @Bind(R.id.updateItemCategory) Button updateItemCategory;

    @Bind(R.id.isLeafNode) CheckBox isLeafNode;




    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    //public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";
    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";

    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;



    ItemCategory itemCategoryForEdit;


    public EditItemCategory() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_category);

        ButterKnife.bind(this);

        itemCategoryForEdit = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);






        if(savedInstanceState==null) {
            // delete previous file in the cache - This will prevent accidently uploading the previous image
            File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
            file.delete();

            //showMessageSnackBar("File delete Status : " + String.valueOf(file.delete()));

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(itemCategoryForEdit!=null) {

            bindDataToEditText();

            loadImage(itemCategoryForEdit.getImagePath());
        }

    }



    void loadImage(String imagePath) {

        Picasso.with(this)
                .load(UtilityGeneral.getImageEndpointURL(this) + imagePath)
                .into(resultView);

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


    void getDataFromEditText(ItemCategory itemCategory)
    {
        if(itemCategory!=null)
        {

            itemCategory.setCategoryName(itemCategoryName.getText().toString());
            itemCategory.setCategoryDescription(itemCategoryDescription.getText().toString());
            itemCategory.setIsLeafNode(isLeafNode.isChecked());
        }

    }



    @OnClick(R.id.updateItemCategory)
    public void updateButtonClick()
    {

        if(itemCategoryForEdit==null)
        {
            return;
        }


        if(isImageChanged)
        {

            // delete previous Image from the Server
            ImageCalls.getInstance()
                    .deleteImage(
                            itemCategoryForEdit.getImagePath(),
                            new DeleteImageCallback()
                    );


            // delete previous image here

            if(isImageRemoved)
            {
                itemCategoryForEdit.setImagePath("");

                updateItemCategory();



            }else
            {

                ImageCalls
                        .getInstance()
                        .uploadPickedImage(
                                this,
                                REQUEST_CODE_READ_EXTERNAL_STORAGE,
                                this
                        );

            }



            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {


            updateItemCategory();

        }
    }





    public void updateItemCategory()
    {



//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UtilityGeneral.getServiceURL(this))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);




        getDataFromEditText(itemCategoryForEdit);


        Call<ResponseBody> itemCategoryCall = itemCategoryService
                                                    .updateItemCategory(itemCategoryForEdit,
                                                            itemCategoryForEdit.getItemCategoryID());


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

                showToastMessage("Network request failed !");
            }

        });

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }



    /*
        Utility Methods
     */





    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);


        isImageChanged = true;
        isImageRemoved = true;
    }


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

        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_CODE_READ_EXTERNAL_STORAGE:

                //uploadPickedImage();

                updateButtonClick();

                break;
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

            showToastMessage("Unable to upload image");
        }

        itemCategoryForEdit.setImagePath(null);

        if(image!=null)
        {
            itemCategoryForEdit.setImagePath(image.getPath());
        }

        updateItemCategory();

    }

    @Override
    public void onFailure(Call<Image> call, Throwable t) {

        showToastMessage("Image Upload failed !");

        itemCategoryForEdit.setImagePath("");

        updateItemCategory();

    }




    private class DeleteImageCallback implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if(response.code()==200)
            {
                showToastMessage("Previous Image Removed !");
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

            showToastMessage("Image remove failed !");

        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
