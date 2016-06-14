package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.ModelStats.DeliveryVehicleSelf;

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

    @GET("/api/DeliveryVehicleSelf")
    Call<List<DeliveryVehicleSelf>> getVehicles(@Query("ShopID") int shopID);

    @GET("/api/DeliveryVehicleSelf/{id}")
    Call<DeliveryVehicleSelf> getVehicle(@Path("id") int id);

    @POST("/api/DeliveryVehicleSelf")
    Call<DeliveryVehicleSelf> postVehicle(@Body DeliveryVehicleSelf deliveryVehicleSelf);

    @PUT("/api/DeliveryVehicleSelf/{id}")
    Call<ResponseBody> putVehicle(@Body DeliveryVehicleSelf deliveryVehicleSelf, @Path("id") int id);

    @DELETE("/api/DeliveryVehicleSelf/{id}")
    Call<ResponseBody> deleteVehicle(@Path("id") int id);

}
