package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderEndPoint;

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
    Call<OrderEndPoint> getOrders(@Query("EndUserID")Integer endUserID,
                                  @Query("ShopID")Integer shopID,
                                  @Query("PickFromShop") Boolean pickFromShop,
                                  @Query("StatusHomeDelivery")Integer homeDeliveryStatus,
                                  @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
                                  @Query("VehicleSelfID")Integer vehicleSelfID,
                                  @Query("PaymentsReceived") Boolean paymentsReceived,
                                  @Query("DeliveryReceived") Boolean deliveryReceived,
                                  @Query("GetDeliveryAddress")Boolean getDeliveryAddress,
                                  @Query("GetStats")Boolean getStats,
                                  @Query("SortBy") String sortBy,
                                  @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                  @Query("metadata_only")Boolean metaonly
    );



    @GET("/api/Order")
    Call<List<Order>> getOrders(@Query("EndUserID")Integer endUserID,
                                @Query("ShopID")Integer shopID,
                                @Query("PickFromShop") Boolean pickFromShop,
                                @Query("StatusHomeDelivery")Integer homeDeliveryStatus,
                                @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
                                @Query("VehicleSelfID")Integer vehicleSelfID,
                                @Query("PaymentsReceived") Boolean paymentsReceived,
                                @Query("DeliveryReceived") Boolean deliveryReceived,
                                @Query("GetDeliveryAddress")Boolean getDeliveryAddress,
                                @Query("GetStats")Boolean getStats
    );




    @GET("/api/Order/{id}")
    Call<Order> getOrder(@Path("id") int orderID);

    @PUT("/api/Order/{OrderID}")
    Call<ResponseBody> putOrder(@Path("OrderID")int orderID,@Body Order order);

    @PUT("/api/Order/ReturnOrder/{OrderID}")
    Call<ResponseBody> returnOrder(@Path("OrderID")int orderID);

    @PUT("/api/Order/CancelByShop/{OrderID}")
    Call<ResponseBody> cancelOrderByShop(@Path("OrderID")int orderID);


    @PUT("/api/Order")
    Call<ResponseBody> putOrderBulk(@Body List<Order> order);

}
