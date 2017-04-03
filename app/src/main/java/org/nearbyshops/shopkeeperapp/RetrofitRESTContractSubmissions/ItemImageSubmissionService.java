package org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions;

import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemImageSubmission;
import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemSubmission;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sumeet on 17/3/17.
 */

public interface ItemImageSubmissionService {


    @POST("/api/v1/ItemImageSubmission")
    Call<ItemImageSubmission> insertItem(@Header("Authorization") String headers,
                                         @Body ItemImageSubmission itemImageSubmission);




    @PUT("/api/v1/ItemImageSubmission/{ItemSubmissionID}")
    Call<ResponseBody> updateItem(@Header("Authorization") String headers,
                                  @Body ItemImageSubmission itemSubmission,
                                  @Path("ItemSubmissionID")int itemSubmissionID);


    //****************************************************************************
}
