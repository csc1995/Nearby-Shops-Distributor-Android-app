package org.nearbyshops.shopkeeperapp.RetrofitRESTContract;

import org.nearbyshops.shopkeeperapp.ModelEndpoints.OrderEndPoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 12/3/16.
 */
public interface OrderServiceDeliveryGuySelf {

    //////////****************** CODE BEGINS ***********************************

    @PUT("/api/Order/DeliveryGuySelf/AcceptOrder/{OrderID}")
    Call<ResponseBody> acceptOrder(@Header("Authorization") String headers,
                                    @Path("OrderID")int orderID);



    @PUT("/api/Order/DeliveryGuySelf/ReturnPackage/{OrderID}")
    Call<ResponseBody> returnOrderPackage(@Header("Authorization") String headers,
                                          @Path("OrderID")int orderID);



    @PUT("/api/Order/DeliveryGuySelf/HandoverToUser/{OrderID}")
    Call<ResponseBody> handoverToUser(@Header("Authorization") String headers,
                                      @Path("OrderID")int orderID);



    @GET("/api/Order/DeliveryGuySelf")
    Call<OrderEndPoint> getOrders(@Header("Authorization") String headers,
                                  @Query("DeliveryGuyID")Integer deliveryGuyID,
                                  @Query("OrderID") Integer orderID,
                                  @Query("EndUserID") Integer endUserID,
                                  @Query("PickFromShop") Boolean pickFromShop,
                                  @Query("StatusHomeDelivery") Integer homeDeliveryStatus,
                                  @Query("StatusPickFromShopStatus") Integer pickFromShopStatus,
                                  @Query("PaymentsReceived") Boolean paymentsReceived,
                                  @Query("DeliveryReceived") Boolean deliveryReceived,
                                  @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
                                  @Query("PendingOrders") Boolean pendingOrders,
                                  @Query("SearchString") String searchString,
                                  @Query("SortBy") String sortBy,
                                  @Query("Limit") Integer limit, @Query("Offset") Integer offset,
                                  @Query("metadata_only") Boolean metaonly);

}
