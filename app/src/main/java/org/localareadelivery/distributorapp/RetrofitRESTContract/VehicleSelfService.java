package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.ModelStats.DeliveryGuySelf;

import java.util.List;

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
public interface VehicleSelfService {

    @GET("/api/DeliveryGuySelf")
    Call<List<DeliveryGuySelf>> getVehicles(@Query("ShopID") int shopID);

    @GET("/api/DeliveryGuySelf/{id}")
    Call<DeliveryGuySelf> getVehicle(@Path("id") int id);

    @POST("/api/DeliveryGuySelf")
    Call<DeliveryGuySelf> postVehicle(@Body DeliveryGuySelf deliveryGuySelf);

    @PUT("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> putVehicle(@Body DeliveryGuySelf deliveryGuySelf, @Path("id") int id);

    @DELETE("/api/DeliveryGuySelf/{id}")
    Call<ResponseBody> deleteVehicle(@Path("id") int id);

}
