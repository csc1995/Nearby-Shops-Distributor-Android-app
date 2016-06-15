package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.Shop;

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
public interface OrderService {

    @GET("/api/Order")
    Call<List<Order>> getOrders(@Query("EndUserID")int endUserID,
                                @Query("ShopID")int shopID,
                                @Query("PickFromShop") Boolean pickFromShop,
                                @Query("StatusHomeDelivery")int homeDeliveryStatus,
                                @Query("StatusFromShopStatus")int pickFromShopStatus,
                                @Query("VehicleSelfID")int vehicleSelfID,
                                @Query("GetDeliveryAddress")boolean getDeliveryAddress,
                                @Query("GetStats")boolean getStats);

    @GET("/api/Order/{id}")
    Call<Order> getOrder(@Path("id") int orderID);

    @PUT("/api/Order/{OrderID}")
    Call<ResponseBody> putOrder(@Path("OrderID")int orderID,@Body Order order);


    @PUT("/api/Order")
    Call<ResponseBody> putOrderBulk(@Body List<Order> order);

}
