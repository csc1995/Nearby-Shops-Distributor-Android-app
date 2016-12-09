package org.localareadelivery.distributorapp.ItemsInStock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.ItemsByCategoryTabsOld.Interfaces.NotifySort;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.Interfaces.NotifyIndicatorChanged;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.Interfaces.NotifySearch;
import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.localareadelivery.distributorapp.ItemsInStockByCat.SlidingLayerSort.UtilitySortItemsInStock;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopItemEndPoint;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;
import org.localareadelivery.distributorapp.ShopHome.UtilityShopHome;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemsInStockFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterItemsInStock.NotificationsFromAdapter, NotifySort, NotifySearch{


//    Map<Integer,ShopItem> shopItemMapTemp = new HashMap<>();

    boolean isDestroyed = false;
    @State boolean show = true;

//    int item_count_item_category = 0;

    private int limit_item = 25;
    int offset_item = 0;
    int item_count_item;
    public int fetched_items_count = 0;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
//    ArrayList<ShopItem> datasetShopItems = new ArrayList<>();

//    @Inject
//    ItemCategoryService itemCategoryService;
    GridLayoutManager layoutManager;
    AdapterItemsInStock listAdapter;

    @Inject
    ShopItemService shopItemService;


    public ItemsInStockFragment() {
        super();
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_in_stock, container, false);

        ButterKnife.bind(this,rootView);

        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }

        notifyItemIndicatorChanged();

        return rootView;
    }



    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }



    void setupRecyclerView()
    {


        listAdapter = new AdapterItemsInStock(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        /*if(dataset.get(position) instanceof ItemCategory)
        {
            final DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

            if(spanCount==0){
                spanCount = 1;
            }

            return (6/spanCount);

        }*/


        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {


                if(position == dataset.size())
                {

                }
                else if(dataset.get(position) instanceof Item)
                {
                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderItemsList)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        /*final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        return (3/spanCount);*/


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestShopItem(false,false);
                    }
                }
            }


        });

    }

//    int offset_previous = -1;

//    @State int previous_position = -1;



    @Override
    public void onRefresh() {
        makeRequestShopItem(true,true);
    }



    void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });

    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }

    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
        }
    }



//    boolean isFirst = true;




/*
    void refreshAdapter()
    {
        dataset.clear();

        HeaderItemsList headerItem = new HeaderItemsList();

        if(searchQuery==null)
        {
            headerItem.setHeading(currentCategory.getCategoryName() + " Items");
        }
        else
        {
            headerItem.setHeading("Search Results (Items)");
        }


        dataset.add(headerItem);
        dataset.addAll(datasetShopItems);
        listAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }
*/



    String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }




    void makeRequestShopItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }

        String current_sort = "";
        current_sort = UtilitySortItemsInStock.getSort(getContext()) + " " + UtilitySortItemsInStock.getAscending(getContext());

        Call<ShopItemEndPoint> endPointCall = null;
        Shop currentShop = UtilityShopHome.getShop(getContext());

        if(searchQuery==null)
        {



            endPointCall = shopItemService.getShopItemEndpoint(
                    null,
                    currentShop.getShopID(),
                    null,null,null,null,null,null,null,
                    null,null,
                    null,null,null,
                    null,current_sort,
                    limit_item,offset_item,false,
                    true);

        }
        else
        {


            endPointCall = shopItemService.getShopItemEndpoint(
                    null,
                    currentShop.getShopID(),
                    null,null,null,null,null,null,null,null,null,
                    null,null,null,
                    searchQuery,current_sort,
                    limit_item,offset_item,false,
                    true);
        }


        endPointCall.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.body()!=null)
                {

                    if(clearDataset)
                    {
                        dataset.clear();
                        HeaderItemsList headerItem = new HeaderItemsList();

                        if(searchQuery==null)
                        {
                            headerItem.setHeading("Items In Stock");
                        }
                        else
                        {
                            headerItem.setHeading("Search Results : Items In Stock");
                        }

                        fetched_items_count = 0;
                        dataset.add(headerItem);
                    }
                    dataset.addAll(response.body().getResults());
                    fetched_items_count = fetched_items_count + response.body().getResults().size();
                    item_count_item = response.body().getItemCount();
                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);

                notifyItemIndicatorChanged();


            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                swipeContainer.setRefreshing(false);
                showToastMessage("Items: Network request failed. Please check your connection !");


            }
        });

    }




//    void resetPreviousPosition()
//    {
//        previous_position = -1;
//    }



    @Override
    public void notifyShopItemUpdated(final ShopItem shopItem) {


        Call<ResponseBody> call = shopItemService.putShopItem(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                shopItem
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {


                    if(shopItem.getItem()!=null)
                    {
                        showToastMessage(shopItem.getItem().getItemName() + " Updated !");

                    }else
                    {
                        showToastMessage("Update Successful !");
                    }

                    //makeNetworkCall();
                }
                else if(response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else if(response.code() == 401)
                {
                    showToastMessage("We are not able to identify you !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });

    }

    @Override
    public void notifyShopItemRemoved(final ShopItem shopItem) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Confirm Remove Item !")
                .setMessage("Do you want to remove this item from your shop !")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        removeShopItem(shopItem);

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Cancelled !");
                    }
                })
                .show();

    }



    void removeShopItem(final ShopItem shopItem)
    {

        Call<ResponseBody> responseBodyCall = shopItemService.deleteShopItem(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                shopItem.getShopID(),
                shopItem.getItemID()
        );

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    if(shopItem.getItem()!=null)
                    {
                        showToastMessage(shopItem.getItem().getItemName() + " Removed !");

                    }else
                    {
                        showToastMessage("Successful !");
                    }


                    int removedPosition = dataset.indexOf(shopItem);
                    dataset.remove(shopItem);
                    listAdapter.notifyItemRemoved(removedPosition);

                    offset_item = offset_item -1;
                    fetched_items_count = fetched_items_count -1;
                    item_count_item = item_count_item -1;
                    notifyItemIndicatorChanged();


                }else if(response.code() == 304) {

                    showToastMessage("Not removed !");

                }
                else if(response.code() == 403)
                {
                    showToastMessage("Not permitted !");
                }
                else if(response.code() == 401)
                {
                    showToastMessage("We are not able to identify you !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }



//                makeRefreshNetworkCall();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");

            }
        });


    }




/*
    @Override
    public boolean backPressed() {

        // reset previous flag
        resetPreviousPosition();

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
//        listAdapter.selectedItems.clear();

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
                makeRefreshNetworkCall();
            }
        }

        return currentCategoryID == -1;
    }
*/


    void notifyItemIndicatorChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity())
                    .notifyItemIndicatorChanged(
                            String.valueOf(fetched_items_count)
                                    + " out of "
                                    + String.valueOf(item_count_item)
                                    + " Items In Stock"
                    );
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }


    // display shop Item Status

}
