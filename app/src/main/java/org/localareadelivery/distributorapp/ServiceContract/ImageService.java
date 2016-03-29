package org.localareadelivery.distributorapp.ServiceContract;

import org.localareadelivery.distributorapp.Model.Image;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sumeet on 29/3/16.
 */
public interface ImageService {


    @POST("/api/Images")
    Call<Image> uploadImage(@Body RequestBody image);


    @Multipart
    @POST("/api/Images")
    Call<Image> uploadImageMultipart(@Part MultipartBody.Part file);

}