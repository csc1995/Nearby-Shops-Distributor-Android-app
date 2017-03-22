package org.nearbyshops.shopkeeperapp.RetrofitRESTContractSubmissions;

import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemSubmission;

import javax.ws.rs.PathParam;

import butterknife.Bind;
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

public interface ItemSubmissionService {


    @POST("/api/v1/ItemSubmission")
    Call<ItemSubmission> insertItem(@Header("Authorization") String headers,
                                    @Body ItemSubmission itemSubmission);


    @PUT("/api/v1/ItemSubmission/{ItemSubmissionID}")
    Call<ResponseBody> updateItem(@Header("Authorization") String headers,
                                  @Body ItemSubmission itemSubmission, @Path("ItemSubmissionID")int itemSubmissionID);




}
