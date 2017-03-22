package org.nearbyshops.shopkeeperapp.Items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.ToggleFab;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifyFABClick;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Utility.UtilitySortItems;
import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.Model.ItemCategory;
import org.nearbyshops.shopkeeperapp.Model.ShopItem;
import org.nearbyshops.shopkeeperapp.ModelEndpoints.ItemEndPoint;
import org.nearbyshops.shopkeeperapp.ModelEndpoints.ShopItemEndPoint;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ItemService;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ItemsFragmentSimple extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterItems.NotificationsFromAdapter , NotifySort , NotifyFABClick, NotifySearch{


    Map<Integer,ShopItem> shopItemMapTemp = new HashMap<>();

    boolean isDestroyed = false;
    @State boolean show = true;

//    int item_count_item_category = 0;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item = 0;
    int fetched_items_count = 0;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
//    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
//    ArrayList<Item> datasetItems = new ArrayList<>();


    GridLayoutManager layoutManager;

    AdapterItems listAdapter;


//    @Inject
//    ItemCategoryService itemCategoryService;


    @Inject
    ShopItemService shopItemService;

    @Inject
    ItemService itemService;


//    ItemCategory currentCategory = null;


    public ItemsFragmentSimple() {
        super();
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_items_simple, container, false);

        ButterKnife.bind(this,rootView);


        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }
        else
        {
            // add this at every rotation
            listAdapter.shopItemMap.putAll(shopItemMapTemp);
        }


        notifyItemHeaderChanged();

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


        listAdapter = new AdapterItems(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {


                if(position == dataset.size())
                {

                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

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
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /*if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
*/

                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page


/*
                    String log = "Dataset Size : " + String.valueOf(dataset.size()) + "\n"
                            + "Last Visible Item Position : " + layoutManager.findLastVisibleItemPosition() + "\n"
                            + "Previous Position : " + previous_position + "\n"
                            + "Offset Item : " + offset_item + "\n"
                            + "Limit Item : " + limit_item + "\n"
                            + "Item Count Item : " + item_count_item;

                    System.out.println(log);

                    Log.d("log_scroll",log);

*/



                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;

                        makeRequestItem(false,false);
                    }

                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).hideFab();
                        }
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;

                    if(show!=previous)
                    {
                        Log.d("scrolllog","hide");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).showFab();
                        }
                    }
                }


            }


        });

    }


//    @State int previous_position = -1;



    @Override
    public void onRefresh() {

//        makeRequestItemCategory();
        makeRequestItem(true,true);
        makeNetworkCallShopItem();
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



    void makeRequestItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = UtilitySortItems.getSort(getContext()) + " " + UtilitySortItems.getAscending(getContext());
/*
        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
                null,
                null,
                null,
                null,null, null, null,
                current_sort, limit_item,offset_item,null);*/

        Call<ItemEndPoint> endPointCall = null;



        //"ITEM_ID"

        if(searchQuery==null)
        {
            endPointCall = itemService.getItemsOuterJoin(
                    null,
                    null,
                    current_sort,
                    limit_item,offset_item, null);
        }
        else
        {
            endPointCall = itemService.getItemsOuterJoin(
                    null,
                    searchQuery,
                    current_sort,
                    limit_item,offset_item, null);
        }


        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    if(clearDataset)
                    {
                        fetched_items_count = 0;
                        dataset.clear();

                        HeaderItemsList headerItem = new HeaderItemsList();

                        if(searchQuery==null)
                        {
                            headerItem.setHeading("Items");
                        }
                        else
                        {
                            headerItem.setHeading("Search Results : Items");
                        }

                        dataset.add(headerItem);
                    }
                    dataset.addAll(response.body().getResults());
                    fetched_items_count = fetched_items_count + response.body().getResults().size();
                    item_count_item = response.body().getItemCount();
                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);

                notifyItemHeaderChanged();


            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                swipeContainer.setRefreshing(false);
                showToastMessage("Items: Network request failed. Please check your connection !");

            }
        });

    }




    @Override
    public void notifyItemSelected() {
        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
            show=true;
        }
    }




    void notifyItemHeaderChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity())
                    .notifyItemIndicatorChanged(
                            String.valueOf(fetched_items_count)
                                    + " out of "
                                    + String.valueOf(item_count_item)
                                    + " "
                                    + " Items"
                    );
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }






    // display shop Item Status


    void makeNetworkCallShopItem()
    {

        int currentShopID = UtilityShopHome.getShop(getContext()).getShopID();


        Call<ShopItemEndPoint> call;


        if(searchQuery!=null)
        {
            /*call = shopItemService.getShopItemEndpoint(
                    null,
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    searchQuery,
                    null,null,null,
                    false,false
            );*/

            call = shopItemService.getShopItemsForShop(
                    null,
                    currentShopID,null,
                    searchQuery,
                    null,null,0
            );

        }
        else
        {

            /*call = shopItemService.getShopItemEndpoint(
                    currentCategory.getItemCategoryID(),
                    currentShopID,
                    null,null,null,null,null,null,null,null,null,null,null,null,
                    null,
                    null,null,null,
                    false,false
            );*/


            call = shopItemService.getShopItemsForShop(
                    null,
                    currentShopID,null,
                    null,
                    null,null,0
            );
        }



        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                if(response.body()!=null && response.code()==200)
                {
                    listAdapter.shopItemMap.clear();

                    for(ShopItem shopItem: response.body().getResults())
                    {
                        listAdapter.shopItemMap.put(shopItem.getItemID(),shopItem);
                    }

                    // add this map into the temporary variable to save shopItems after rotation
                    shopItemMapTemp.putAll(listAdapter.shopItemMap);

                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your network !");
            }
        });
    }






    void addSelectedToShopClick()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(UtilityShopHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemCreateBulkRequest(tempShopItemList);

    }


    private void makeShopItemCreateBulkRequest(List<ShopItem> tempShopItemList) {


        Call<ResponseBody> call = shopItemService.createShopItemBulk(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }

                makeNetworkCallShopItem();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }




    void removeSeletedShopItemClick(){



        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        List<ShopItem> tempShopItemList = new ArrayList<>();

        for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
        {
//            entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());


            ShopItem shopItem = new ShopItem();
            shopItem.setShopID(UtilityShopHome.getShop(getContext()).getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemDeleteBulkRequest(tempShopItemList);
    }




    private void makeShopItemDeleteBulkRequest(List<ShopItem> tempShopItemList) {

        Call<ResponseBody> call = shopItemService.deleteShopItemBulk(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                tempShopItemList
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                {
                    showToastMessage("Update Successful !");

                    clearSelectedItems();

                }else if (response.code() == 206)
                {
                    showToastMessage("Partially Updated. Check data changes !");

                    clearSelectedItems();

                }else if(response.code() == 304)
                {

                    showToastMessage("No item updated !");

                }else
                {
                    showToastMessage("Unknown server error or response !");
                }

                makeNetworkCallShopItem();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }



    void clearSelectedItems()
    {
        // clear the selected items
        listAdapter.selectedItems.clear();
    }


    @Override
    public void addItem() {

    }



    @Override
    public void removeSelectedFromShop() {
//        showToastMessage("Remove Selected");

        removeSeletedShopItemClick();
    }

    @Override
    public void addSelectedToShop() {

//        showToastMessage("Add Selected !");
        addSelectedToShopClick();
    }


}
