package org.localareadelivery.distributorapp.RetrofitRESTContract;

import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopItemEndPoint;

import java.util.List;

import javax.ws.rs.QueryParam;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by sumeet on 14/3/16.
 */
public interface ShopItemService {


    @POST("/api/v1/ShopItem")
    Call<ResponseBody> postShopItem(@Header("Authorization") String headers,
                                    @Body ShopItem shopItem);

    @PUT("/api/v1/ShopItem")
    Call<ResponseBody> putShopItem(@Header("Authorization") String headers,
                                   @Body ShopItem shopItem);

    @DELETE("/api/v1/ShopItem")
    Call<ResponseBody> deleteShopItem(@Header("Authorization") String headers,
                                      @Query("ShopID")int ShopID, @Query("ItemID") int itemID);


    @GET("/api/v1/ShopItem")
    Call<ShopItemEndPoint> getShopItemEndpoint(
            @Query("ItemCategoryID")Integer ItemCategoryID,
            @Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
            @Query("latCenter")Double latCenter,@Query("lonCenter")Double lonCenter,
            @Query("deliveryRangeMax")Double deliveryRangeMax,
            @Query("deliveryRangeMin")Double deliveryRangeMin,
            @Query("proximity")Double proximity,
            @Query("EndUserID") Integer endUserID,@Query("IsFilledCart") Boolean isFilledCart,
            @Query("IsOutOfStock") Boolean isOutOfStock,@Query("PriceEqualsZero")Boolean priceEqualsZero,
            @Query("MinPrice")Integer minPrice,@Query("MaxPrice")Integer maxPrice,
            @Query("SearchString") String searchString,
            @Query("SortBy") String sortBy,
            @Query("Limit") Integer limit, @Query("Offset") Integer offset,
            @Query("metadata_only")Boolean metaonly,
            @Query("GetExtras")Boolean getExtras
    );




    // bulk update methods


    @POST("/api/v1/ShopItem/CreateBulk")
    Call<ResponseBody> createShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);

    @POST("/api/v1/ShopItem/DeleteBulk")
    Call<ResponseBody> deleteShopItemBulk(@Header("Authorization") String headers,
                                          @Body List<ShopItem> itemList);





    // Deprecated methods


    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID")Integer itemCategoryID);



    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID")Integer itemCategoryID,
                                      @Query("IsOutOfStock") Boolean isOutOfStock,@Query("PriceEqualsZero")Boolean priceEqualsZero);


    @GET("/api/v1/ShopItem/Deprecated")
    Call<List<ShopItem>> getShopItems(@Query("ShopID")Integer ShopID, @Query("ItemID") Integer itemID,
                                      @Query("ItemCategoryID")Integer itemCategoryID,
                                      @Query("IsOutOfStock") Boolean isOutOfStock,@Query("PriceEqualsZero")Boolean priceEqualsZero,
                                      @Query("SortBy") String sortBy,
                                      @Query("Limit") Integer limit, @Query("Offset") Integer offset
    );

}
