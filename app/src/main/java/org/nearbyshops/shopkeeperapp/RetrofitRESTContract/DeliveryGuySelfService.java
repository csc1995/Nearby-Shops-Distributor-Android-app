package org.nearbyshops.shopkeeperapp.RetrofitRESTContract;

import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.Model.Image;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface DeliveryGuySelfService {



    @POST("/api/DeliveryGuySelf")
    Call<DeliveryGuySelf> postVehicle(@Header("Authorization") String headers,
                                      @Body DeliveryGuySelf deliveryGuySelf);

    @PUT("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> putVehicle(@Header("Authorization") String headers,
                                  @Body DeliveryGuySelf deliveryGuySelf, @Path("id") int id);


    @PUT("/api/DeliveryGuySelf/UpdateBySelf/{DeliveryGuyID}")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                    @Path("DeliveryGuyID")int deliveryGuyID,
                                    @Body DeliveryGuySelf deliveryGuySelf);



    @DELETE("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> deleteVehicle(@Header("Authorization") String headers,
                                     @Path("id") int id);


    @GET("/api/DeliveryGuySelf/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username")String username);

    @GET("/api/DeliveryGuySelf/Login")
    Call<DeliveryGuySelf> getLogin(@Header("Authorization") String headers);

    @GET("/api/DeliveryGuySelf")
    Call<List<DeliveryGuySelf>> getVehicles(@Header("Authorization") String headers,
                                            @Query("ShopID") int shopID,
                                            @Query("IsEnabled") Boolean isEnabled);


//    @GET("/api/DeliveryGuySelf/{id}")
//    Call<DeliveryGuySelf> getVehicle(@Header("Authorization") String headers,
//                                     @Path("id") int id);



    // Image Endpoints



    @POST("/api/DeliveryGuySelf/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName

    @DELETE("/api/DeliveryGuySelf/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name")String fileName);

}
