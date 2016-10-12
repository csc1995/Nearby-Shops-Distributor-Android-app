package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.Distributor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 12/3/16.
 */
public interface DistributorService {

    @GET("/api/v1/Distributor/Login")
    Call<Distributor> loginDistributor(@Header("Authorization") String headers);

    @POST("/api/v1/Distributor")
    Call<Distributor> postDistributor(@Body Distributor distributor);

    @PUT("/api/v1/Distributor/{id}")
    Call<ResponseBody> putDistributor(@Body Distributor distributor, @Path("id") int id);

    @DELETE("/api/v1/Distributor/{id}")
    Call<ResponseBody> deleteDistributor(@Path("id") int id);

}
