package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.DeliveryGuySelf;
import org.localareadelivery.distributorapp.Model.Image;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface DeliveryGuySelfService {


    @GET("/api/DeliveryGuySelf")
    Call<List<DeliveryGuySelf>> getVehicles(@Query("ShopID") int shopID,
                                            @Query("IsEnabled") Boolean isEnabled);


    @GET("/api/DeliveryGuySelf/{id}")
    Call<DeliveryGuySelf> getVehicle(@Path("id") int id);

    @POST("/api/DeliveryGuySelf")
    Call<DeliveryGuySelf> postVehicle(@Body DeliveryGuySelf deliveryGuySelf);

    @PUT("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> putVehicle(@Body DeliveryGuySelf deliveryGuySelf, @Path("id") int id);

    @DELETE("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> deleteVehicle(@Path("id") int id);




    @POST("/api/DeliveryGuySelf/Image")
    Call<Image> uploadImage(@Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName

    @DELETE("/api/DeliveryGuySelf/Image/{name}")
    Call<ResponseBody> deleteImage(@Path("name")String fileName);



    @GET("/api/DeliveryGuySelf/CheckUsernameExists/{username}")
    Call<ResponseBody> checkUsernameExist(@Path("username")String username);

}
