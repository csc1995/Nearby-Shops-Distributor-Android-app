package org.localareadelivery.distributorapp.deprecatedCode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by sumeet on 30/3/16.
 */
public class ImageUpload {




    /*
    //imageUri = resultUri;

    try {
        //Getting the Bitmap from Gallery

        // /Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
        //Setting the Bitmap to ImageView

        //resultView.setImageBitmap(bitmap);
    } catch (Exception e) {
        e.printStackTrace();
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



    //ShopService shopService = retrofit.create(ShopService.class);

    //Call<Image> shopCall = imageService.uploadImage();



    /*



    public void uploadImage()
    {


        //String filePath = getFilePath(this,imageUri);


        // WOrking
        //String filePath2 = getRealPathFromURI(imageUri);

        //String filepath3 = getRealPathFromURI2(imageUri);


        //byte [] data = BitmapUtility.getBitmapToBytes(((BitmapDrawable) resultView.getDrawable()).getBitmap());


        //Log.d("applog",imageUri.toString());

        //multipart/form-data
        // create RequestBody instance from file
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

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);


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



    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

    {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
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




*/




}
