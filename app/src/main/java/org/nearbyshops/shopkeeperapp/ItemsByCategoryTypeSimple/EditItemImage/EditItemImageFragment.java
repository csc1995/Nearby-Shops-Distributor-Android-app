package org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.EditItemImage;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.Model.Image;
import org.nearbyshops.shopkeeperapp.Model.ItemImage;
import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemImageSubmission;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions.ItemImageSubmissionService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemImageService;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

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

import static android.app.Activity.RESULT_OK;


public class EditItemImageFragment extends Fragment {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    public static final String ITEM_ID_INTENT_KEY = "item_id_intent_key";

    @Inject
    ItemImageService itemImageService;

    @Inject ItemImageSubmissionService itemImageSubmissionService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @Bind(R.id.uploadImage) ImageView resultView;


    @Bind(R.id.imageID) EditText imageID;
    @Bind(R.id.captionTitle) EditText captionTitle;
    @Bind(R.id.caption) EditText caption;
    @Bind(R.id.image_copyrights_info) EditText imageCopyrightsInfo;
    @Bind(R.id.imageOrder) EditText imageOrder;

    @Bind(R.id.saveButton) Button buttonUpdateItem;


    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    public static final String IS_UPDATE_INTENT_KEY = "is_update_intent_key";
    boolean isUpdate = false;



    int current_mode = MODE_ADD;

    boolean isDestroyed = false;


//    Item item = new Item();
    ItemImage itemImage = new ItemImage();

    ItemImageSubmission itemImageSubmission = new ItemImageSubmission();


    public EditItemImageFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_item_image_fragment, container, false);

        ButterKnife.bind(this,rootView);

        isUpdate = getActivity().getIntent().getBooleanExtra(IS_UPDATE_INTENT_KEY,false);


        if(savedInstanceState==null)
        {

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);


            if (current_mode == MODE_ADD)
            {

//                if(getActivity().getActionBar()!=null)
//                {
//                    getActivity().getActionBar().setTitle("Add Item Image");
//                }


                if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
                {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Item Image");
                }

//                item.setItemCategoryID(itemCategory.getItemCategoryID());
//                System.out.println("Item Category ID : " + item.getItemCategoryID());
            }



            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }


        if(UtilityItemImage.getItemImage(getContext())!=null)
        {
            itemImage = UtilityItemImage.getItemImage(getContext());
        }


        if(itemImage !=null) {
            bindDataToViews();
        }



        updateIDFieldVisibility();


        if(itemImage !=null) {
            loadImage(itemImage.getImageFilename());
        }


        showLogMessage("Inside On Create View !");

        return rootView;
    }



    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {

            imageID.setVisibility(View.GONE);

            if(isUpdate)
            {

                if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
                {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Item Image");
                }
            }
            else
            {
                buttonUpdateItem.setText("Add Item Image");
            }

        }
        else if(current_mode== MODE_UPDATE)
        {
            imageID.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
        }

    }




    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }




    void loadImage(String filename) {

//        String iamgepath = UtilityGeneral.getServiceURL(getContext()) + "/api/v1/ItemImage/five_hundred_" + imagePath + ".jpg";


        String imagePath = UtilityGeneral.getServiceURL(getActivity()) + "/api/v1/ItemImage/Image/five_hundred_"
                + filename + ".jpg";

        System.out.println(imagePath);

        Picasso.with(getContext())
                .load(imagePath)
                .into(resultView);
    }




    @OnClick(R.id.saveButton)
    public void UpdateButtonClick()
    {

        if(!validateData())
        {
//            showToastMessage("Please correct form data before save !");
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addAccount();
        }
        else if(current_mode == MODE_UPDATE)
        {
            update();
        }
    }


    boolean validateData()
    {
        boolean isValid = true;

        if(captionTitle.getText().toString().length()==0)
        {
            captionTitle.setError("Item Name cannot be empty !");
            captionTitle.requestFocus();
            isValid= false;
        }



        return isValid;
    }




    void addAccount()
    {
        if(isImageChanged)
        {
            if(!isImageRemoved)
            {
                // upload image with add
                uploadPickedImage(false);
            }


            // reset the flags
            isImageChanged = false;
            isImageRemoved = false;

        }
        else
        {
            // post request
            retrofitPOSTRequest();
        }

    }


    void update()
    {

        if(isImageChanged)
        {


            // delete previous Image from the Server
            deleteImage(itemImage.getImageFilename());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                itemImage.setImageFilename(null);
                retrofitPUTRequest();

            }else
            {

                uploadPickedImage(true);
            }


            // resetting the flag in order to ensure that future updates do not upload the same image again to the server
            isImageChanged = false;
            isImageRemoved = false;

        }else {

            retrofitPUTRequest();
        }
    }



    void bindDataToViews()
    {
        if(itemImage !=null) {

            imageID.setText(String.valueOf(itemImage.getImageID()));
            captionTitle.setText(itemImage.getCaptionTitle());
            caption.setText(itemImage.getCaption());
            imageCopyrightsInfo.setText(itemImage.getImageCopyrights());
            imageOrder.setText(String.valueOf(itemImage.getImageOrder()));

        }
    }





    void getDataFromViews()
    {


        if(!imageID.getText().toString().equals(""))
        {
            itemImage.setImageID(Integer.parseInt(imageID.getText().toString()));
        }

        itemImage.setCaptionTitle(captionTitle.getText().toString());
        itemImage.setCaption(caption.getText().toString());
        itemImage.setImageCopyrights(imageCopyrightsInfo.getText().toString());

        if(!imageOrder.getText().toString().equals(""))
        {
            itemImage.setImageOrder(Integer.parseInt(imageOrder.getText().toString()));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    public void retrofitPUTRequest()
    {

        getDataFromViews();

        itemImageSubmission.setItemImage(itemImage);

//        showToastMessage(itemImage.getImageFilename());


        Call<ResponseBody> call = itemImageSubmissionService.updateItem(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                itemImageSubmission,itemImageSubmission.getItemImageSubmissionID()
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Update Failed Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Update failed !");
            }
        });

    }





    void retrofitPOSTRequest()
    {
        getDataFromViews();
        itemImage.setItemID(getActivity().getIntent().getIntExtra(ITEM_ID_INTENT_KEY,0));

        itemImageSubmission.setItemImage(itemImage);

//        showToastMessage(itemImage.getImageFilename());

        if(isUpdate)
        {
            itemImageSubmission.setItemImageID(itemImage.getImageID());
        }
        else
        {
            itemImageSubmission.setItemImageID(null);
        }




        Call<ItemImageSubmission> call = itemImageSubmissionService.insertItem(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                itemImageSubmission);



        call.enqueue(new Callback<ItemImageSubmission>() {
            @Override
            public void onResponse(Call<ItemImageSubmission> call, Response<ItemImageSubmission> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    itemImageSubmission = response.body();
                    bindDataToViews();

                }
                else if(response.code()== 403 || response.code() ==401)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Add failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ItemImageSubmission> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Add failed !");
            }
        });


    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }





    /*
        Utility Methods
     */




    void showToastMessage(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }




    @Bind(R.id.textChangePicture)
    TextView changePicture;


    @OnClick(R.id.removePicture)
    void removeImage()
    {

        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();

        resultView.setImageDrawable(null);

        isImageChanged = true;
        isImageRemoved = true;
    }



    public static void clearCache(Context context)
    {
        File file = new File(context.getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");
        file.delete();
    }



    @OnClick(R.id.textChangePicture)
    void pickShopImage() {

//        ImageCropUtility.showFileChooser(()getActivity());



        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }



        clearCache(getContext());

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        super.onActivityResult(requestCode, resultCode, result);



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && result != null
                && result.getData() != null) {


            Uri filePath = result.getData();

            //imageUri = filePath;

            if (filePath != null) {

                startCropActivity(result.getData(),getContext());
            }

        }


        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }
    }



    // upload image after being picked up
    void startCropActivity(Uri sourceUri, Context context) {



        final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.jpeg";

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(getContext(),R.color.blueGrey800));
        options.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);


        // this function takes the file from the source URI and saves in into the destination URI location.
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(context,this);

        //.withMaxResultSize(400,300)
        //.withMaxResultSize(500, 400)
        //.withAspectRatio(16, 9)
    }





    /*

    // Code for Uploading Image

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showToastMessage("Permission Granted !");
                    pickShopImage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    showToastMessage("Permission Denied for Read External Storage . ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }





    public void uploadPickedImage(final boolean isModeEdit)
    {

        Log.d("applog", "onClickUploadImage");


        // code for checking the Read External Storage Permission and granting it.
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);

            return;
        }


        File file = new File(getContext().getCacheDir().getPath() + "/" + "SampleCropImage.jpeg");


        // Marker

        RequestBody requestBodyBinary = null;

        InputStream in = null;

        try {
            in = new FileInputStream(file);

            byte[] buf;
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;

            requestBodyBinary = RequestBody.create(MediaType.parse("application/octet-stream"), buf);

        } catch (Exception e) {
            e.printStackTrace();
        }



        Call<Image> imageCall = itemImageService.uploadItemImage(UtilityLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    itemImage.setImageFilename(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    itemImage.setImageFilename(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    itemImage.setImageFilename(null);

                }

                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }


            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



                showToastMessage("Image Upload failed !");
                itemImage.setImageFilename(null);


                if(isModeEdit)
                {
                    retrofitPUTRequest();
                }
                else
                {
                    retrofitPOSTRequest();
                }
            }
        });

    }



    void deleteImage(String filename)
    {
        Call<ResponseBody> call = itemImageService.deleteItemImage(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if(isDestroyed)
                {
                    return;
                }




                if(response.code()==200)
                    {
                        showToastMessage("Image Removed !");
                    }
                    else
                    {
//                        showToastMessage("Image Delete failed");
                    }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }



//                showToastMessage("Image Delete failed");
            }
        });
    }


}
