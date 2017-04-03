package org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.EditItem;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.FilterItemsBySpecifications.FilterItemsActivity;
import org.nearbyshops.shopkeeperapp.FilterItemsBySpecifications.FilterItemsFragment;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.EditItemImage.EditItemImage;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.EditItemImage.EditItemImageFragment;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.EditItemImage.UtilityItemImage;
import org.nearbyshops.shopkeeperapp.Model.Image;
import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.Model.ItemCategory;
import org.nearbyshops.shopkeeperapp.Model.ItemImage;
import org.nearbyshops.shopkeeperapp.ModelItemSpecification.EndPoints.ItemImageEndPoint;
import org.nearbyshops.shopkeeperapp.ModelItemSpecification.ItemSpecificationName;
import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemSubmission;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions.ItemSubmissionService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemImageService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTItemSpecs.ItemSpecNameService;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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


public class EditItemFragmentNew extends Fragment implements AdapterItemImages.notificationsFromAdapter, AdapterItemSpecifications.NotifyItemSpecs {

    public static int PICK_IMAGE_REQUEST = 21;
    // Upload the image after picked up
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 56;


    ItemCategory itemCategory;

    public static final String ITEM_CATEGORY_INTENT_KEY = "item_cat";

//    Validator validator;


//    @Inject
//    DeliveryGuySelfService deliveryService;

    @Inject ItemSubmissionService itemSubmissionService;
    @Inject ItemService itemService;
    @Inject ItemImageService itemImageService;
    @Inject ItemSpecNameService itemSpecNameService;


    // flag for knowing whether the image is changed or not
    boolean isImageChanged = false;
    boolean isImageRemoved = false;


    // bind views
    @Bind(R.id.uploadImage)
    ImageView resultView;



    @Bind(R.id.itemID) EditText itemID;
    @Bind(R.id.itemName) EditText itemName;
    @Bind(R.id.itemDescription) EditText itemDescription;
    @Bind(R.id.itemDescriptionLong) EditText itemDescriptionLong;
    @Bind(R.id.quantityUnit) EditText quantityUnit;
    @Bind(R.id.barcode_image) ImageView barcodeSign;


    @Bind(R.id.image_copyrights) TextView imageCopyrights;
    @Bind(R.id.list_price) EditText listPrice;
    String barcode;
    String barcodeFormat;

    @Bind(R.id.saveButton) Button buttonUpdateItem;

    public static final String ITEM_INTENT_KEY = "item_intent_key";

    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final String IS_UPDATE_INTENT_KEY = "is_update_intent_key";
    boolean isUpdate = false;

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;




//    DeliveryGuySelf deliveryGuySelf = new DeliveryGuySelf();
//    ShopAdmin shopAdmin = new ShopAdmin();
        Item item = new Item();

    ItemSubmission itemSubmission = new ItemSubmission();

    public EditItemFragmentNew() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_item_fragment_new, container, false);
        ButterKnife.bind(this,rootView);

        isUpdate = getActivity().getIntent().getBooleanExtra(IS_UPDATE_INTENT_KEY,false);

        if(savedInstanceState==null)
        {
//            shopAdmin = getActivity().getIntent().getParcelableExtra(SHOP_ADMIN_INTENT_KEY);

            current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);
            itemCategory = getActivity().getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);



            if(current_mode == MODE_UPDATE)
            {
//                item = UtilityItem.getItem(getContext());


            }
            else if (current_mode == MODE_ADD)
            {
                item.setItemCategoryID(itemCategory.getItemCategoryID());
//                System.out.println("Item Category ID : " + item.getItemCategoryID());


                String itemJson = getActivity().getIntent().getStringExtra(ITEM_INTENT_KEY);

                Gson gson = new Gson();
                item = gson.fromJson(itemJson, Item.class);


            }


            if(item !=null) {
                bindDataToViews();
            }
            else
            {
                item = new Item();
            }



            showLogMessage("Inside OnCreateView - Saved Instance State !");
        }



//        if(validator==null)
//        {
//            validator = new Validator(this);
//            validator.setValidationListener(this);
//        }

        updateIDFieldVisibility();


        if(item !=null) {
            loadImage(item.getItemImageURL());
            showLogMessage("Inside OnCreateView : DeliveryGUySelf : Not Null !");
        }


        showLogMessage("Inside On Create View !");

        setupRecyclerView();
        setupRecyclerViewSpecs();


        // bind barcodes
//        barcodeResults.setText("Barcode : " + item.getBarcode() + "\nFormat : " + item.getBarcodeFormat());

        if(item!=null && item.getBarcode()!=null && item.getBarcodeFormat()!=null)
        {
            barcodeResults.setText("Barcode : " + item.getBarcode() + "\nFormat : " + item.getBarcodeFormat());
        }


        return rootView;
    }






    ArrayList<ItemImage> dataset = new ArrayList<>();
    @Bind(R.id.recyclerview_item_images)
    RecyclerView itemImagesList;
    AdapterItemImages adapterItemImages;
    GridLayoutManager layoutManager;


    void setupRecyclerView() {

        adapterItemImages = new AdapterItemImages(dataset,getActivity(),this);
        itemImagesList.setAdapter(adapterItemImages);
        layoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
        itemImagesList.setLayoutManager(layoutManager);

        makeNetworkCallItemImages(true);
    }



    ArrayList<ItemSpecificationName> datasetSpecs = new ArrayList<>();
    @Bind(R.id.recyclerview_item_specifications)
    RecyclerView itemSpecsList;
    AdapterItemSpecifications adapterItemSpecs;
    GridLayoutManager layoutManagerItemSpecs;


    void setupRecyclerViewSpecs()
    {
        adapterItemSpecs = new AdapterItemSpecifications(datasetSpecs,getActivity(),this);
        itemSpecsList.setAdapter(adapterItemSpecs);
        layoutManagerItemSpecs = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        itemSpecsList.setLayoutManager(layoutManagerItemSpecs);

        makeNetworkCallSpecs(true);

    }


    void makeNetworkCallSpecs(final boolean clearDataset)
    {
        if(!isUpdate)
        {
            return;
        }


        Call<List<ItemSpecificationName>> call = itemSpecNameService.getItemSpecName(
          item.getItemID(),null
        );


        call.enqueue(new Callback<List<ItemSpecificationName>>() {
            @Override
            public void onResponse(Call<List<ItemSpecificationName>> call, Response<List<ItemSpecificationName>> response) {

                if(response.code()==200)
                {
                    if(clearDataset)
                    {
                        datasetSpecs.clear();
                    }

                    datasetSpecs.addAll(response.body());

                    adapterItemSpecs.notifyDataSetChanged();
                }
                else if(response.code() == 401 || response.code() == 403)
                {
                    showToastMessage("Not Permitted");
                }
                else
                {
                    showToastMessage("Failed to load specs : code " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<List<ItemSpecificationName>> call, Throwable t) {

                showToastMessage("Failed !");
            }
        });
    }


    @OnClick(R.id.sync_refresh_item_spec)
    void syncRefreshItemSpecs()
    {
        makeNetworkCallSpecs(true);
    }



    void makeNetworkCallItemImages(final boolean clearDataset)
    {


            Call<ItemImageEndPoint> call = itemImageService.getItemImages(
                    item.getItemID(),ItemImage.IMAGE_ORDER,null,null,null
            );


            call.enqueue(new Callback<ItemImageEndPoint>() {
                @Override
                public void onResponse(Call<ItemImageEndPoint> call, Response<ItemImageEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!=null)
                    {
                        if(response.body().getResults()!=null)
                        {
                            if(clearDataset)
                            {
                                dataset.clear();
                            }

                            dataset.addAll(response.body().getResults());
                            adapterItemImages.notifyDataSetChanged();


//                            showToastMessage("Dataset Changed : Item ID : " + String.valueOf(item.getItemID()) +
//                            "\nDataset Count" + String.valueOf(response.body().getResults().size())
//                            );

                        }
                    }
                }

                @Override
                public void onFailure(Call<ItemImageEndPoint> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }


                    showToastMessage("Loading Images Failed !");
                }
            });

    }




    boolean isDestroyed = false;

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





    void updateIDFieldVisibility()
    {

        if(isUpdate)
        {
            itemID.setVisibility(View.VISIBLE);
        }
        else
        {
            itemID.setVisibility(View.GONE);
        }


        if(current_mode==MODE_ADD)
        {
            if(isUpdate)
            {
//                itemID.setVisibility(View.VISIBLE);
                buttonUpdateItem.setText("Update Item");
            }
            else
            {
//                itemID.setVisibility(View.GONE);
                buttonUpdateItem.setText("Add Item");
            }

        }
        else if(current_mode== MODE_UPDATE)
        {
//            itemID.setVisibility(View.VISIBLE);
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

        String iamgepath = UtilityGeneral.getServiceURL(getContext()) + "/api/v1/Item/Image/" + imagePath;

        System.out.println(iamgepath);

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

//            item = new Item();
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

        if(itemName.getText().toString().length()==0)
        {
            itemName.setError("Item Name cannot be empty !");
            itemName.requestFocus();
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
            deleteImage(item.getItemImageURL());

            /*ImageCalls.getInstance()
                    .deleteImage(
                            itemForEdit.getItemImageURL(),
                            new DeleteImageCallback()
                    );*/


            if(isImageRemoved)
            {

                item.setItemImageURL(null);
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
        if(item !=null) {

            itemID.setText(String.valueOf(item.getItemID()));
            itemName.setText(item.getItemName());
            itemDescription.setText(item.getItemDescription());

            quantityUnit.setText(item.getQuantityUnit());
            itemDescriptionLong.setText(item.getItemDescriptionLong());

//            item.setBarcode(barcode);
//            item.setBarcodeFormat(barcodeFormat);





            listPrice.setText(String.valueOf(item.getListPrice()));

            imageCopyrights.setText(item.getImageCopyrights());

            if(item.getBarcode()!=null && item.getBarcodeFormat()!=null)
            {
                barcodeResults.setText("Barcode : " + item.getBarcode() + "\nFormat : " + item.getBarcodeFormat());
            }


        }
    }





    void getDataFromViews()
    {
        if(item ==null)
        {
            if(current_mode == MODE_ADD)
            {
//                item = new Item();
            }
            else
            {
                return;
            }

            item = new Item();
        }

//        if(current_mode == MODE_ADD)
//        {
//            deliveryGuySelf.setShopID(UtilityShopHome.getShop(getContext()).getShopID());
//        }


        item.setItemName(itemName.getText().toString());
        item.setItemDescription(itemDescription.getText().toString());

        item.setItemDescriptionLong(itemDescriptionLong.getText().toString());
        item.setQuantityUnit(quantityUnit.getText().toString());


        if(!listPrice.getText().toString().equals(""))
        {
            item.setListPrice(Float.parseFloat(listPrice.getText().toString()));
        }

        item.setImageCopyrights(imageCopyrights.getText().toString());
        item.setBarcode(barcode);
        item.setBarcodeFormat(barcodeFormat);
    }



    public void retrofitPUTRequest()
    {

        getDataFromViews();

        itemSubmission.setItem(item);



        Call<ResponseBody> call = itemSubmissionService.updateItem(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                itemSubmission,itemSubmission.getItemSubmissionID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                    UtilityItem.saveItem(item,getContext());
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

                showToastMessage("Update failed !");
            }
        });

    }





    void retrofitPOSTRequest()
    {

        getDataFromViews();
        item.setItemCategoryID(itemCategory.getItemCategoryID());


        itemSubmission.setItem(item);

        if(isUpdate)
        {
            itemSubmission.setItemID(item.getItemID());
        }
        else
        {
            itemSubmission.setItemID(null);
        }


//        System.out.println("Item Category ID (POST) : " + item.getItemCategoryID());



        Call<ItemSubmission> call = itemSubmissionService.insertItem(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                itemSubmission);


        call.enqueue(new Callback<ItemSubmission>() {
            @Override
            public void onResponse(Call<ItemSubmission> call, Response<ItemSubmission> response) {

                if(response.code()==201)
                {
                    showToastMessage("Add successful !");

                    current_mode = MODE_UPDATE;
                    updateIDFieldVisibility();
                    itemSubmission = response.body();
                    bindDataToViews();

//                    UtilityItem.saveItem(item,getContext());

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
            public void onFailure(Call<ItemSubmission> call, Throwable t) {

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
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP)
        {

            resultView.setImageURI(null);
            resultView.setImageURI(UCrop.getOutput(result));

            isImageChanged = true;
            isImageRemoved = false;


        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(result);

        }
        else
        {
            IntentResult resultLocal = IntentIntegrator.parseActivityResult(requestCode, resultCode, result);
            if(result != null) {
                if(resultLocal.getContents() == null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(), "Scanned: " + resultLocal.getContents(), Toast.LENGTH_LONG).show();
                    String resultsText = "Barcode : " + resultLocal.getContents();
                    resultsText = resultsText + "\nFormat : " + resultLocal.getFormatName();

//                    barcodeResults.setText(resultLocal.getContents());
//                    barcodeResults.setText(resultLocal.getFormatName());

                    item.setBarcode(resultLocal.getContents());
                    item.setBarcodeFormat(resultLocal.getFormatName());
                    barcode = resultLocal.getContents();
                    barcodeFormat = resultLocal.getFormatName();

                    barcodeResults.setText(resultsText);

                }
            } else {
                super.onActivityResult(requestCode, resultCode, result);
            }

        }





    }


    @Bind(R.id.barcode_results) TextView barcodeResults;


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



        Call<Image> imageCall = itemService.uploadImage(UtilityLogin.getAuthorizationHeaders(getContext()),
                requestBodyBinary);


        imageCall.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {

                if(response.code()==201)
                {
//                    showToastMessage("Image UPload Success !");

                    Image image = response.body();
                    // check if needed or not . If not needed then remove this line
//                    loadImage(image.getPath());


                    item.setItemImageURL(image.getPath());

                }
                else if(response.code()==417)
                {
                    showToastMessage("Cant Upload Image. Image Size should not exceed 2 MB.");

                    item.setItemImageURL(null);

                }
                else
                {
                    showToastMessage("Image Upload failed !");
                    item.setItemImageURL(null);

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
                item.setItemImageURL(null);


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
        Call<ResponseBody> call = itemService.deleteImage(
                UtilityLogin.getAuthorizationHeaders(getContext()),
                filename);



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


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

//                showToastMessage("Image Delete failed");
            }
        });
    }



    @Override
    public void addItemImage() {

        Intent intent = new Intent(getActivity(), EditItemImage.class);
        intent.putExtra(EditItemImageFragment.EDIT_MODE_INTENT_KEY,EditItemImageFragment.MODE_ADD);
        intent.putExtra(EditItemImageFragment.ITEM_ID_INTENT_KEY,item.getItemID());
        startActivity(intent);

    }


    @Override
    public void editItemImage(ItemImage itemImage, int position) {

        Intent intent = new Intent(getActivity(), EditItemImage.class);

        intent.putExtra(EditItemImageFragment.EDIT_MODE_INTENT_KEY,EditItemImageFragment.MODE_ADD);
        intent.putExtra(EditItemImageFragment.ITEM_ID_INTENT_KEY,item.getItemID());
        intent.putExtra(EditItemImageFragment.IS_UPDATE_INTENT_KEY,true);
        UtilityItemImage.saveItemImage(itemImage,getActivity());

        startActivity(intent);
    }


    @OnClick(R.id.sync_refresh)
    void syncRefreshClick()
    {
        makeNetworkCallItemImages(true);
    }


    @Override
    public void removeItemImage(final ItemImage itemImage, final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Delete Item Image !")
                .setMessage("Are you sure you want to delete this Item Image ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        makeRequestDeleteItemImage(itemImage, position);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        showToastMessage("Cancelled !");
                    }
                })
                .show();
    }






    void makeRequestDeleteItemImage(ItemImage itemImage, final int position)
    {
        Call<ResponseBody> call = itemImageService.deleteItemImageData(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                itemImage.getImageID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Removed !");

                    dataset.remove(position);
                    adapterItemImages.notifyItemRemoved(position+1);


                }
                else if(response.code()==401 || response.code()==403)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Failed to Delete !");
            }
        });

    }




    @Override
    public void addItemSpec() {

//        @OnClick(R.id.item_specifications)
//        void addItemSpecifications()
//        {
            Intent intent = new Intent(getActivity(), FilterItemsActivity.class);
            intent.putExtra(FilterItemsFragment.ITEM_ID_INTENT_KEY,item.getItemID());
            startActivity(intent);
//        }
    }





    @OnClick(R.id.barcode_image)
    void setupBarcodes() {
//        IntentIntegrator integrator = new IntentIntegrator(getActivity());
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scan a barcode");
//        integrator.setCameraId(0);  // Use a specific camera of the device
//        integrator.setBeepEnabled(false);
//        integrator.setBarcodeImageEnabled(true);
//        integrator.initiateScan();


        IntentIntegrator.forSupportFragment(this)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("Scan a barcode")
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true)
                .setOrientationLocked(false)
                .initiateScan();

    }



}

