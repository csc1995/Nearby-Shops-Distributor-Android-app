package org.nearbyshops.shopkeeperapp.RetrofitRESTContractPFS;

import org.nearbyshops.shopkeeperapp.ModelPickFromShop.OrderEndPointPFS;
import org.nearbyshops.shopkeeperapp.ModelPickFromShop.OrderItemEndPointPFS;
import org.nearbyshops.shopkeeperapp.ModelPickFromShop.OrderPFS;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface OrderServiceShopStaffPFS {



    @PUT("/api/OrderPFS/ShopStaff/SetConfirmed/{OrderID}")
    Call<ResponseBody> confirmOrderPFS(@Header("Authorization") String headers,
                                    @Path("OrderID")int orderID);




    @PUT("/api/OrderPFS/ShopStaff/SetOrderPacked/{OrderID}")
    Call<ResponseBody> setOrderPackedPFS(@Header("Authorization") String headers,
                                         @Path("OrderID")int orderID);




    @PUT("/api/OrderPFS/ShopStaff/SetOrderReadyForPickup/{OrderID}")
    Call<ResponseBody> setOrderReadyForPickupPFS(@Header("Authorization") String headers,
                                                 @Path("OrderID")int orderID);





    @PUT("/api/OrderPFS/ShopStaff/MarkDelivered/{OrderID}")
    Call<ResponseBody> markDeliveredPFS(@Header("Authorization") String headers,
                                        @Path("OrderID")int orderID);




    @PUT("/api/OrderPFS/ShopStaff/PaymentReceived/{OrderID}")
    Call<ResponseBody> paymentReceivedPFS(@Header("Authorization") String headers,
                                          @Path("OrderID")int orderID);




    @PUT("/api/OrderPFS/ShopStaff/PaymentReceived")
    Call<ResponseBody> paymentReceivedBulk(@Header("Authorization") String headers,
                                           @Body List<OrderPFS> ordersList);





    @PUT("/api/OrderPFS/ShopStaff/CancelByShop/{OrderID}")
    Call<ResponseBody> cancelByShopPFS(@Header("Authorization") String headers,
                                       @Path("OrderID")int orderID);


    @GET("/api/OrderPFS/ShopStaff")
    Call<OrderEndPointPFS> getOrdersPFS(@Header("Authorization") String headers,
                                         @Query("OrderID")Integer orderID,
                                         @Query("EndUserID")Integer endUserID,
                                         @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
                                         @Query("PaymentsReceived") Boolean paymentsReceived,
                                         @Query("DeliveryReceived") Boolean deliveryReceived,
                                         @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
                                         @Query("PendingOrders") Boolean pendingOrders,
                                         @Query("SearchString") String searchString,
                                         @Query("SortBy") String sortBy,
                                         @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                         @Query("metadata_only")Boolean metaonly);


    // order Item Endpoint

    @GET("/api/OrderItemPFS")
    Call<OrderItemEndPointPFS> getOrderItemPFS(@Header("Authorization") String headers,
                                               @Query("OrderID")Integer orderID,
                                               @Query("ItemID")Integer itemID,
                                               @Query("SearchString")String searchString,
                                               @Query("SortBy") String sortBy,
                                               @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                               @Query("metadata_only")Boolean metaonly);

}
