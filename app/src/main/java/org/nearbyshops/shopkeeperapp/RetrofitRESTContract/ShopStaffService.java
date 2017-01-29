package org.nearbyshops.shopkeeperapp.RetrofitRESTContract;

import org.nearbyshops.shopkeeperapp.Model.Image;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;

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
public interface ShopStaffService {

    @POST("/api/v1/ShopStaff")
    Call<ShopStaff> postShopStaff(@Header("Authorization") String headers,
                              @Body ShopStaff staff);


    @PUT("/api/v1/ShopStaff/UpdateByShopAdmin")
    Call<ResponseBody> putShopStaff(@Header("Authorization") String headers,
                                @Body ShopStaff staff);


    @PUT("/api/v1/ShopStaff/UpdateBySelf")
    Call<ResponseBody> updateBySelf(@Header("Authorization") String headers,
                                         @Body ShopStaff staff);


    @DELETE("/api/v1/ShopStaff/{StaffID}")
    Call<ResponseBody> deleteShopStaff(@Header("Authorization") String headers,
                                   @Path("StaffID") int staffID);




    @GET("/api/v1/ShopStaff/ForShopAdmin")
    Call<List<ShopStaff>> getShopStaff(@Header("Authorization") String headers,
                                           @Query("IsEnabled") Boolean isEnabled);


    @GET("/api/v1/ShopStaff/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username") String username);


    @GET("/api/v1/ShopStaff/Login")
    Call<ShopStaff> getLogin(@Header("Authorization") String headers);






    // Image Calls

    @POST("/api/v1/ShopStaff/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/ShopStaff/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);







    //-------------------------------------------------------

//    @GET("/api/v1/Staff/LoginScreen")
//    Call<Staff> loginStaff(@Header("Authorization") String headers);

//    @POST("/api/v1/Staff")
//    Call<Staff> postStaff(@Body Staff staff);

//    @PUT("/api/v1/Staff/{id}")
//    Call<ResponseBody> putStaff(@Body Staff staff, @Path("id") int id);

//    @DELETE("/api/v1/Staff/{id}")
//    Call<ResponseBody> deleteStaff(@Path("id") int id);

}
