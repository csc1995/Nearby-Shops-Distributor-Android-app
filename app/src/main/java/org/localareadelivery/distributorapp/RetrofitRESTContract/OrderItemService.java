package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.OrderItem;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderEndPoint;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderItemEndPoint;

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
public interface OrderItemService {


    @GET("/api/OrderItem")
    Call<OrderItemEndPoint> getOrderItem(@Query("OrderID")Integer orderID,
                                         @Query("ItemID")Integer itemID,
                                         @Query("SearchString")String searchString,
                                         @Query("SortBy") String sortBy,
                                         @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                         @Query("metadata_only")Boolean metaonly);


    @DELETE("/api/OrderItem")
    Call<ResponseBody> deleteOrderItem(@Query("OrderID")int orderID, @Query("ItemID") int itemID);


    @PUT("/api/OrderItem")
    Call<ResponseBody> updateOrderItem(@Body OrderItem orderItem);


    @POST("/api/OrderItem")
    Call<ResponseBody> createOrderItem(@Body OrderItem orderItem);

}