package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderEndPoint;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
public interface OrderServiceShopStaff {


    @PUT("/api/Order/ShopStaffAccounts/SetConfirmed/{OrderID}")
    Call<ResponseBody> confirmOrder(@Header("Authorization") String headers,
                                    @Path("OrderID")int orderID);


    @PUT("/api/Order/ShopStaffAccounts/SetOrderPacked/{OrderID}")
    Call<ResponseBody> setOrderPacked(@Header("Authorization") String headers,
                                      @Path("OrderID")int orderID);



//    @PUT("/api/Order/ShopStaffAccounts/HandoverToDelivery/{OrderID}")
//    Call<ResponseBody> handoverToDelivery(@Path("OrderID")int orderID,@Body Order order);


    @PUT("/api/Order/ShopStaffAccounts/HandoverToDelivery/{DeliveryGuySelfID}")
    Call<ResponseBody> handoverToDelivery(@Header("Authorization") String headers,
                                          @Path("DeliveryGuySelfID")int deliveryGuyID,
                                          @Body List<Order> ordersList);



    @PUT("/api/Order/ShopStaffAccounts/UndoHandover/{OrderID}")
    Call<ResponseBody> undoHandover(@Header("Authorization") String headers,
                                    @Path("OrderID")int orderID);



    @PUT("/api/Order/ShopStaffAccounts/MarkDelivered/{OrderID}")
    Call<ResponseBody> markDelivered(@Header("Authorization") String headers,
                                     @Path("OrderID")int orderID);


    @PUT("/api/Order/ShopStaffAccounts/PaymentReceived/{OrderID}")
    Call<ResponseBody> paymentReceived(@Header("Authorization") String headers,
                                       @Path("OrderID")int orderID);


    @PUT("/api/Order/ShopStaffAccounts/PaymentReceived")
    Call<ResponseBody> paymentReceivedBulk(@Header("Authorization") String headers,
                                           @Body List<Order> ordersList);


    @PUT("/api/Order/ShopStaffAccounts/CancelByShop/{OrderID}")
    Call<ResponseBody> cancelledByShop(@Header("Authorization") String headers,
                                       @Path("OrderID")int orderID);




    @PUT("/api/Order/ShopStaffAccounts/AcceptReturnCancelledByShop/{OrderID}")
    Call<ResponseBody> acceptReturnCancelledByShop(@Header("Authorization") String headers,
                                                   @Path("OrderID")int orderID);


    @PUT("/api/Order/ShopStaffAccounts/AcceptReturn/{OrderID}")
    Call<ResponseBody> acceptReturn(@Header("Authorization") String headers,
                                    @Path("OrderID")int orderID);





    // previous methods

//    @GET("/api/Order/{id}")
//    Call<Order> getOrder(@Path("id") int orderID);

//    @PUT("/api/Order/{OrderID}")
//    Call<ResponseBody> putOrder(@Path("OrderID") int orderID, @Body Order order);

//    @PUT("/api/Order/ReturnOrder/{OrderID}")
//    Call<ResponseBody> returnOrder(@Path("OrderID") int orderID);

//    @PUT("/api/Order/CancelByShop/{OrderID}")
//    Call<ResponseBody> cancelOrderByShop(@Path("OrderID") int orderID);

}
