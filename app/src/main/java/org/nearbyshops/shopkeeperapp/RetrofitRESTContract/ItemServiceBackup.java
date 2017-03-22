package org.nearbyshops.shopkeeperapp.RetrofitRESTContract;

import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.ModelEndpoints.ItemEndPoint;

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
 * Created by sumeet on 3/4/16.
 */
public interface ItemServiceBackup
{

    @POST("/api/v1/Item")
    Call<Item> insertItem(@Body Item item);

    @PUT("/api/v1/Item/{id}")
    Call<ResponseBody> updateItem(@Body Item item, @Path("id") int id);

    @DELETE("/api/v1/Item/{id}")
    Call<ResponseBody> deleteItem(@Path("id") int id);


    @PUT("/api/v1/Item/")
    Call<ResponseBody> updateItemBulk(@Body List<Item> itemList);


    @GET("/api/v1/Item")
    Call<ItemEndPoint> getItems(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("ShopID") Integer shopID,
            @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
            @Query("deliveryRangeMax") Double deliveryRangeMax,
            @Query("deliveryRangeMin") Double deliveryRangeMin,
            @Query("proximity") Double proximity,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );



    @GET("/api/v1/Item/OuterJoin")
    Call<ItemEndPoint> getItemsOuterJoin(
            @Query("ItemCategoryID") Integer itemCategoryID,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only") Boolean metaonly
    );

    @GET("/api/v1/Item/{id}")
    Call<Item> getItem(@Path("id") int ItemID);




    // Deprecated Calls

    @GET("/api/v1/Item/Deprecated")
    Call<List<Item>> getItems(@Query("ItemCategoryID") int itemCategoryID, @Query("ShopID") int shopID);

    @GET("/api/v1/Item/Deprecated")
    Call<List<Item>> getItems(@Query("ItemCategoryID") int itemCategoryID);


}
