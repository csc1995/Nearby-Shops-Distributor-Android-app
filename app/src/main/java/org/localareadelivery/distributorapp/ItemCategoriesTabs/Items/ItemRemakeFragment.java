package org.localareadelivery.distributorapp.ItemCategoriesTabs.Items;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.NotifyCategoryChanged;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.NotifyFabClick_Item;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.NotifyGeneral;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.NotifySort;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.NotifyTitleChanged;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.Interfaces.ToggleFab;
import org.localareadelivery.distributorapp.ItemCategoriesTabs.ItemCategoriesTabs;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelEndpoints.ItemEndPoint;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopItemEndPoint;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParent;
import org.localareadelivery.distributorapp.Utility.UtilitySortItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRemakeFragment extends Fragment
        implements  ItemAdapterTwo.NotificationReceiver,
        SwipeRefreshLayout.OnRefreshListener,
        NotifyCategoryChanged, NotifyFabClick_Item, NotifySort {

    public static final String ADD_ITEM_INTENT_KEY = "add_item_intent_key";

    @State
    ArrayList<Item> dataset = new ArrayList<>();

    RecyclerView itemCategoriesList;
    ItemAdapterTwo listAdapter;
    GridLayoutManager layoutManager;


    @Inject
    ShopItemService shopItemService;

    @State boolean show = true;
//    boolean isDragged = false;

    @Inject
    ItemService itemService;


    NotifyGeneral notificationReceiverFragment;

//    NotifyTitleChanged notifyTitleChanged;

    @State
    ItemCategory notifiedCurrentCategory = null;


    // scroll variables
    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;



    public ItemRemakeFragment() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }


    int getMaxChildCount(int spanCount, int heightPixels)
    {
       return (spanCount * (heightPixels/250));
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_item_remake, container, false);

        ButterKnife.bind(this,rootView);

        itemCategoriesList = (RecyclerView)rootView.findViewById(R.id.recyclerViewItemCategories);



        if(getActivity() instanceof ItemCategoriesTabs)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();
            activity.notifyCategoryChanged = this;
            activity.notifyFabClick_item = this;
        }

        if(getActivity() instanceof NotifyGeneral)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();

            this.notificationReceiverFragment = (NotifyGeneral) activity;
        }

/*
        if(getActivity() instanceof NotifyTitleChanged)
        {
            notifyTitleChanged = (NotifyTitleChanged)getActivity();
        }
*/


        if(savedInstanceState==null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                        dataset.clear();
                        makeRequestRetrofit(false);
                    makeNetworkCallShopItem();
                }
            });

        }
        else
        {
            onViewStateRestored(savedInstanceState);
        }


        setupRecyclerView();
        setupSwipeContainer();


        return  rootView;

    }



    void setupRecyclerView()
    {


        listAdapter = new ItemAdapterTwo(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        itemCategoriesList.setLayoutManager(layoutManager);



        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        layoutManager.setSpanCount(metrics.widthPixels/350);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition() == dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeRequestRetrofit(false);
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

                        if(getActivity() instanceof ToggleFab){
                            ((ToggleFab)getActivity()).hideFab();
                        }

                    }

                }else if(dy < -20)
                {

                    boolean previous = show;
                    show = true;

                    if(show!=previous)
                    {
                        // changed
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

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }





    public void makeRequestRetrofit(final boolean isbackPressed)
    {
        if(notifiedCurrentCategory==null)
        {
            swipeContainer.setRefreshing(false);
            return;
        }


        String current_sort = "";

        current_sort = UtilitySortItem.getSort(getContext()) + " " + UtilitySortItem.getAscending(getContext());

        Call<ItemEndPoint> endPointCall = itemService.getItemsOuterJoin(notifiedCurrentCategory.getItemCategoryID(),
                current_sort, limit,offset, null);

        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {

                if(response.body()!=null) {

                    dataset.addAll(response.body().getResults());
                    item_count = response.body().getItemCount();
                }

                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();
                notifyTitleChanged();


                if(!notifiedCurrentCategory.getAbstractNode() && item_count>0 && !isbackPressed)
                {
                    notificationReceiverFragment.notifySwipeToright();
                }

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

//                Log.d("errorlog",t.toString());
                swipeContainer.setRefreshing(false);

            }
        });
    }



    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }



    void notifyDelete()
    {
        onRefresh();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    if(parentCategory.getAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");

                        return;
                    }
//                    listAdapter.getRequestedChangeParent().setParentCategoryID(parentCategory.getItemCategoryID());

                    listAdapter.getRequestedChangeParent().setItemCategoryID(parentCategory.getItemCategoryID());

                    makeUpdateRequest(listAdapter.getRequestedChangeParent());



                }
            }
        }

        if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                ItemCategory parentCategory = data.getParcelableExtra("result");

                if(parentCategory!=null)
                {

                    if(parentCategory.getAbstractNode())
                    {
                        showToastMessage(parentCategory.getCategoryName()
                                + " is an abstract category you cannot add item to an abstract category");

                        return;
                    }

                    List<Item> tempList = new ArrayList<>();

                    for(Map.Entry<Integer,Item> entry : listAdapter.selectedItems.entrySet())
                    {
                        entry.getValue().setItemCategoryID(parentCategory.getItemCategoryID());
                        tempList.add(entry.getValue());
                    }

                    makeRequestBulk(tempList);
                }

            }
        }
    }



    void makeUpdateRequest(Item item)
    {

//        Call<ResponseBody> call2 = itemCategoryService.updateItemCategory(itemCategory,itemCategory.getItemCategoryID());

        Call<ResponseBody> call = itemService.updateItem(item,item.getItemID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Change Parent Successful !");

                    onRefresh();

                }else
                {
                    showToastMessage("Change Parent Failed !");
                }

                listAdapter.setRequestedChangeParent(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

                listAdapter.setRequestedChangeParent(null);

            }
        });

    }



    void changeParentBulk()
    {

        if(listAdapter.selectedItems.size()==0)
        {
            showToastMessage("No item selected. Please make a selection !");

            return;
        }

        // make an exclude list. Put selected items to an exclude list. This is done to preven a category to make itself or its
        // children its parent. This is logically incorrect and should not happen.

        ItemCategoriesParent.clearExcludeList();
//        ItemCategoriesParent.excludeList.putAll(listAdapter.selectedItems);

        Intent intentParent = new Intent(getActivity(), ItemCategoriesParent.class);
        startActivityForResult(intentParent,2,null);
    }


    void makeRequestBulk(final List<Item> list)
    {
//        Call<ResponseBody> call = itemService.updateItemCategoryBulk(list);

        Call<ResponseBody> call = itemService.updateItemBulk(list);
//        Call<ResponseBody> call = null;
//
//
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



//                makeRequestRetrofit();

                onRefresh();
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
    public void notifyItemSelected() {

        // show fab if it is hidden
        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }




    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0 ; // reset the offset
        makeRequestRetrofit(false);
        makeNetworkCallShopItem();


        Log.d("applog","refreshed");
    }


    @Override
    public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {

        notifiedCurrentCategory = currentCategory;
        dataset.clear();
        offset = 0 ; // reset the offset
        makeRequestRetrofit(isBackPressed);
        makeNetworkCallShopItem();
    }




    void notifyTitleChanged()
    {
        String name = "";

        if(notifiedCurrentCategory!=null)
        {
            name = notifiedCurrentCategory.getCategoryName();
        }


        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged( name +
                            " Items (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);
//        outState.putParcelable("currentCategory",notifiedCurrentCategory);
//        outState.putParcelableArrayList("dataset",dataset);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Icepick.restoreInstanceState(this,savedInstanceState);
        notifyTitleChanged();

//        listAdapter.notifyDataSetChanged();
//        setupRecyclerView();

        if(savedInstanceState!=null)
        {
//            notifiedCurrentCategory = savedInstanceState.getParcelable("currentCategory");
//            ArrayList<Item> tempCat = savedInstanceState.getParcelableArrayList("dataset");
//            dataset.clear();
//            dataset.addAll(tempCat);
        }

    }

    @Override
    public void detachSelectedClick() {
        removeSeletedShopItemClick(null);
    }

    @Override
    public void changeParentForSelected() {

//        changeParentBulk();
        addSelectedToShopClick(null);
    }

    @Override
    public void addItem() {
        addItemClick();
    }

    @Override
    public void addfromGlobal() {

    }


    void addItemClick()
    {
        Intent addIntent = new Intent(getActivity(), AddItem.class);
        addIntent.putExtra(ADD_ITEM_INTENT_KEY,notifiedCurrentCategory);
        startActivity(addIntent);
    }

    @Override
    public void notifySortChanged() {
        onRefresh();
    }





    // add / remove shop Items Begins


//    @OnClick(R.id.add_selected_bulk)
    void addSelectedToShopClick(View view)
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
            shopItem.setShopID(ApplicationState.getInstance().getCurrentShop().getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemCreateBulkRequest(tempShopItemList);

    }


    private void makeShopItemCreateBulkRequest(List<ShopItem> tempShopItemList) {


        Call<ResponseBody> call = shopItemService.createShopItemBulk(tempShopItemList);


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

                onRefresh();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }



//    @OnClick(R.id.remove_selected)
    void removeSeletedShopItemClick(View view){



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
            shopItem.setShopID(ApplicationState.getInstance().getCurrentShop().getShopID());
            shopItem.setItemID(entry.getValue().getItemID());

            tempShopItemList.add(shopItem);
        }

        makeShopItemDeleteBulkRequest(tempShopItemList);
    }





    private void makeShopItemDeleteBulkRequest(List<ShopItem> tempShopItemList) {

        Call<ResponseBody> call = shopItemService.deleteShopItemBulk(tempShopItemList);


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

                onRefresh();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network Request failed. Check your internet / network connection !");

            }
        });

    }









    void makeNetworkCallShopItem()
    {

        int currentShopID = ApplicationState.getInstance().getCurrentShop().getShopID();

        if(notifiedCurrentCategory==null)
        {

            swipeContainer.setRefreshing(false);

            return;
        }

        Call<ShopItemEndPoint> call = shopItemService.getShopItemEndpoint(notifiedCurrentCategory.getItemCategoryID(),
                currentShopID,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,false);




        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                listAdapter.shopItemMap.clear();

                for(ShopItem shopItem: response.body().getResults())
                {
                    listAdapter.shopItemMap.put(shopItem.getItemID(),shopItem);
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your network !");
            }
        });
    }

    // Add/ remove shop Items Ends

}
