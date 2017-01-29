package org.nearbyshops.shopkeeperapp.DeliveryAccounts.EditProfile;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.Model.Image;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.Utility.ImageCropUtility;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;


public class EditDeliveryFragment extends Fragment {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


//    Validator validator;


    @Inject
    DeliveryGuySelfService deliveryService;

    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @Bind(R.id.uploadImage)
    ImageView resultView;


    @Bind(R.id.item_id) EditText item_id;
    @Bind(R.id.name) EditText name;
    @Bind(R.id.username) EditText username;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.about) EditText about;

    @Bind(R.id.phone_number) EditText phone;
    @Bind(R.id.switch_enable) Switch aSwitch;


    @Bind(R.id.designation) EditText designation;
    @Bind(R.id.make_account_private) CheckBox makeAccountPrivate;
    @Bind(R.id.govt_id_name) EditText govtIDName;
    @Bind(R.id.govt_id_number) EditText govtIDNumber;


    @Bind(R.id.saveButton) Button buttonUpdateItem;


    public static final String DELIVERY_GUY_INTENT_KEY = "delivery_guy_intent_key";
    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;

    DeliveryGuySelf deliveryGuySelf = new DeliveryGuySelf();


    public EditDeliveryFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    Subscription editTextSub;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_delivery_guy, container, false);

        ButterKnife.bind(this,rootView);

        if(savedInstanceState==null)
        {
//            deliveryGuySelf = getActivity().getIntent().getParcelableExtra(DELIVERY_GUY_INTENT_KEY);

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

            if(current_mode==MODE_UPDATE)
            {
                deliveryGuySelf = UtilityDeliveryGuySelf.getDeliveryGuySelf(getActivity());

                if(deliveryGuySelf!=null) {
                    bindDataToViews();
                }
            }

//            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        if(deliveryGuySelf!=null) {
            loadImage(deliveryGuySelf.getProfileImageURL());
            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }



        editTextSub = RxTextView
                .textChanges(username)
                .debounce(700, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence value) {
                        // do some work with new text
                        usernameCheck();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        System.out.println(throwable.toString());

                    }
                });


        showLogMessage("Inside On Create View !");

        return rootView;
    }

    void updateIDFieldVisibility()
    {

        if(current_mode==MODE_ADD)
        {
            buttonUpdateItem.setText("Create Account");
            item_id.setVisibility(View.GONE);
        }
        else if(current_mode== MODE_UPDATE)
        {
            item_id.setVisibility(View.VISIBLE);
            buttonUpdateItem.setText("Save");
        }
    }


    public static final String TAG_LOG = "TAG_LOG";

    void showLogMessage(String message)
    {
        Log.i(TAG_LOG,message);
        System.out.println(message);
    }



    void loadImage(String imagePath) {

        String iamgepath = UtilityGeneral.getServiceURL(getContext()) + "/api/DeliveryGuySelf/Image/" + imagePath;

        Picasso.with(getContext())
                .load(iamgepath)
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
            deliveryGuySelf = new DeliveryGuySelf();
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


        if(phone.getText().toString().length()==0)
        {
            phone.setError("Please enter Phone Number");
            phone.requestFocus();
            isValid= false;
        }


        if(password.getText().toString().length()==0)
        {
            password.requestFocus();
            password.setError("Password cannot be empty");
            isValid = false;
        }


        if(username.getText().toString().length()==0)
        {
            username.requestFocus();
            username.setError("Username cannot be empty");
            isValid= false;
        }


        if(name.getText().toString().length()==0)
        {

//            Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_close_black_24dp);
            name.requestFocus();
            name.setError("Name cannot be empty");
            isValid = false;
        }


        return isValid;
    }


//    @OnTextChanged(R.id.username)
    void usernameCheck()
    {


        if(deliveryGuySelf!=null && deliveryGuySelf.getUsername()!=null
                &&
                username.getText().toString().equals(deliveryGuySelf.getUsername()))
        {
            username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
            return;
        }


        Call<ResponseBody> call = deliveryService.checkUsernameExist(username.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    //username already exists
                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_4));
                    username.setError("Username already exist !");
                }
                else if(response.code() == 204)
                {
                    username.setTextColor(ContextCompat.getColor(getContext(),R.color.gplus_color_1));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
            deleteImage(deliveryGuySelf.getProfileImageURL());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                deliveryGuySelf.setProfileImageURL(null);
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
        if(deliveryGuySelf!=null) {

            item_id.setText(String.valueOf(deliveryGuySelf.getDeliveryGuyID()));
            name.setText(deliveryGuySelf.getName());
            username.setText(deliveryGuySelf.getUsername());
            password.setText(deliveryGuySelf.getPassword());
            about.setText(deliveryGuySelf.getAbout());
            phone.setText(deliveryGuySelf.getPhoneNumber());
            aSwitch.setChecked(deliveryGuySelf.getEnabled());

            designation.setText(deliveryGuySelf.getDesignation());
            govtIDName.setText(deliveryGuySelf.getGovtIDName());
            govtIDNumber.setText(deliveryGuySelf.getGovtIDNumber());
            makeAccountPrivate.setChecked(deliveryGuySelf.isAccountPrivate());
        }
    }


    void getDataFromViews()
    {
        if(deliveryGuySelf==null)
        {
            if(current_mode == MODE_ADD)
            {
                deliveryGuySelf = new DeliveryGuySelf();
            }
            else
            {
                return;
            }
        }

        if(current_mode == MODE_ADD)
        {
            deliveryGuySelf.setShopID(UtilityShopHome.getShop(getContext()).getShopID());
        }

        deliveryGuySelf.setName(name.getText().toString());
        deliveryGuySelf.setUsername(username.getText().toString());
        deliveryGuySelf.setPassword(password.getText().toString());
        deliveryGuySelf.setAbout(about.getText().toString());
        deliveryGuySelf.setPhoneNumber(phone.getText().toString());

        deliveryGuySelf.setEnabled(aSwitch.isChecked());

        deliveryGuySelf.setDesignation(designation.getText().toString());
        deliveryGuySelf.setGovtIDName(govtIDName.getText().toString());
        deliveryGuySelf.setGovtIDNumber(govtIDNumber.getText().toString());
        deliveryGuySelf.setAccountPrivate(makeAccountPrivate.isChecked());

    }



    public void retrofitPUTRequest()
    {

        getDataFromViews();

        // update Item Call
        Call<ResponseBody> call = deliveryService.putVehicle(UtilityLogin.getAuthorizationHeaders(getContext()),
                deliveryGuySelf,deliveryGuySelf.getDeliveryGuyID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");

                }
                else
                {
                    showToastMessage("Update Failed !");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showToastMessage("Update Failed !");
            }
        });

    }


    void retrofitPOSTRequest()
    {
        getDataFromViews();

        Call<DeliveryGuySelf> call = deliveryService.postVehicle(UtilityLogin.getAuthorizationHeaders(getContext()),
                deliveryGuySelf);

        call.enqueue(new Callback<DeliveryGuySelf>() {
            @Override
            public void onResponse(Call<DeliveryGuySelf> call, Response<DeliveryGuySelf> response) {

                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    deliveryGuySelf = response.body();
                    bindDataToViews();
                }
                else
                {
                    showToastMessage("Add failed !");
                }


            }

            @Override
            public void onFailure(Call<DeliveryGuySelf> call, Throwable t) {

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



        if (requestCode == ImageCropUtility.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
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



        Call<Image> imageCall = deliveryService.uploadImage(UtilityLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(response.code()==201)
                {
                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    deliveryGuySelf.setProfileImageURL(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    deliveryGuySelf.setProfileImageURL(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    deliveryGuySelf.setProfileImageURL(null);

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

                showToastMessage("Image Upload failed !");
                deliveryGuySelf.setProfileImageURL(null);

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
        Call<ResponseBody> call = deliveryService.deleteImage(UtilityLogin.getAuthorizationHeaders(getContext()),filename);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Image Delete successful !");
                }
                else
                {
                    showToastMessage("Image Delete failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Image Delete failed");
            }
        });
    }


}
