package org.localareadelivery.distributorapp.AddItemsToShopInventory.Items;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.localareadelivery.distributorapp.AddItemsToShopInventory.FragmentsNotificationReceiver;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.ItemCategoriesTabs;
import org.localareadelivery.distributorapp.AddItemsToShopInventory.NotifyPagerAdapter;
import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelEndpoints.ItemEndPoint;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopItemEndPoint;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;
import org.localareadelivery.distributorapp.SelectParent.ItemCategoriesParent;

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

public class ItemRemakeFragmentOld extends Fragment
        implements  ItemRemakeAdapter.NotificationReceiver, SwipeRefreshLayout.OnRefreshListener, ItemCategoriesTabs.ReceiveNotificationFromTabsForItems {

    public static final String ADD_ITEM_INTENT_KEY = "add_item_intent_key";

    ArrayList<Item> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    ItemRemakeAdapter listAdapter;

    GridLayoutManager layoutManager;

//    @Inject
//    ItemCategoryDataRouter dataRouter;


    @State boolean show = false;
//    boolean isDragged = false;

    @Inject
    ItemService itemService;

    @Inject
    ShopItemService shopItemService;


//    @Bind(R.id.tablayout)
//    TabLayout tabLayout;

    @Bind(R.id.options)
    RelativeLayout options;

    @Bind(R.id.appbar)
    AppBarLayout appBar;


    FragmentsNotificationReceiver notificationReceiverFragment;

    NotifyPagerAdapter notifyPagerAdapter;




    @State
    ItemCategory notifiedCurrentCategory = null;


    // scroll variables
    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;



    public ItemRemakeFragmentOld() {
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


        View rootView = inflater.inflate(R.layout.fragment_item_remake_old, container, false);

        ButterKnife.bind(this,rootView);

        itemCategoriesList = (RecyclerView)rootView.findViewById(R.id.recyclerViewItemCategories);

        setupRecyclerView();
        setupSwipeContainer();



        if(getActivity() instanceof ItemCategoriesTabs)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();
            activity.setTabsNotificationReceiver(this);
        }

        if(getActivity() instanceof FragmentsNotificationReceiver)
        {
            ItemCategoriesTabs activity = (ItemCategoriesTabs)getActivity();

            this.notificationReceiverFragment = (FragmentsNotificationReceiver) activity;
        }

        if(getActivity() instanceof NotifyPagerAdapter)
        {
            notifyPagerAdapter = (NotifyPagerAdapter)getActivity();
        }


        if(savedInstanceState==null)
        {

             swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                        dataset.clear();
                        makeRequestRetrofit();
                    makeNetworkCallShopItem();

                }
            });

        }



        return  rootView;

    }



    void setupRecyclerView()
    {


        listAdapter = new ItemRemakeAdapter(dataset,getActivity(),this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        itemCategoriesList.setLayoutManager(layoutManager);



        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        layoutManager.setSpanCount(metrics.widthPixels/350);


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
                        makeRequestRetrofit();
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

//                        options.animate().translationX(metrics.widthPixels-10);
//                        options.animate().translationY(200);

                        options.setVisibility(View.VISIBLE);

                        notificationReceiverFragment.showAppBar();
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;



                    if(show!=previous)
                    {
                        // changed
//                        options.setVisibility(View.VISIBLE);
//                        options.animate().translationX(0);
                        Log.d("scrolllog","hide");

//                        options.animate().translationY(0);

                        options.setVisibility(View.GONE);
                        notificationReceiverFragment.hideAppBar();
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





    public void makeRequestRetrofit()
    {
        if(notifiedCurrentCategory==null)
        {

            swipeContainer.setRefreshing(false);

            return;
        }


        Call<ItemEndPoint> endPointCall = itemService.getItems(notifiedCurrentCategory.getItemCategoryID(),
                null,null,null,null,null,null,null,
                limit,offset,
                null);

        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                item_count = response.body().getItemCount();

                if(response.body()!=null) {

                    dataset.addAll(response.body().getResults());
                }

                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();

                if(notifyPagerAdapter!=null)
                {
//                    notifyPagerAdapter.NotifyTitleChanged("Items (" + String.valueOf(item_count) + ")",1);
                    notifyTitleChanged();
                }

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");
                swipeContainer.setRefreshing(false);

            }
        });


        /*Call<List<Item>> itemCategoryCall = itemService.getItems(notifiedCurrentCategory.getItemCategoryID());


        itemCategoryCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

//                dataset.clear();





                if(response.body()!=null) {

                    dataset.addAll(response.body());
                }

                swipeContainer.setRefreshing(false);
                listAdapter.notifyDataSetChanged();

                if(notifyPagerAdapter!=null)
                {
                    notifyPagerAdapter.NotifyTitleChanged("Items (" + String.valueOf(dataset.size()) + ")",1);
                }

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

                swipeContainer.setRefreshing(false);

            }
        });
*/

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
//        dataset.clear();
//        offset = 0; // reset the offset
//        makeRequestRetrofit();

        onRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();



//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeContainer.setRefreshing(true);
//
//                try {
//
//                    makeRequestRetrofit();
//
//                } catch (IllegalArgumentException ex)
//                {
//                    ex.printStackTrace();
//
//                }
//            }
//        });



    }


//    private boolean isRootCategory = true;
//
//    private ArrayList<String> categoryTree = new ArrayList<>();




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

//                    dataset.clear();
//                    offset = 0 ; // reset the offset
//                    makeRequestRetrofit();

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


    @OnClick(R.id.changeParentBulk)
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


    void exitFullScreen()
    {
        options.setVisibility(View.VISIBLE);
        notificationReceiverFragment.showAppBar();


        if(show)
        {
            show= false;
        }else
        {
            show=true;
        }
    }

    @Override
    public void notifyItemCategorySelected() {

        exitFullScreen();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @OnClick(R.id.addItemCategory)
    void addItemCategoryClick()
    {
        Intent addIntent = new Intent(getActivity(), AddItemOld.class);

        addIntent.putExtra(ADD_ITEM_INTENT_KEY,notifiedCurrentCategory);

        startActivity(addIntent);

//        addIntent.putExtra(AddItemCategoryOld.ADD_ITEM_CATEGORY_INTENT_KEY,currentCategory);
//
//        startActivity(addIntent);

    }


    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0 ; // reset the offset
        makeRequestRetrofit();
        makeNetworkCallShopItem();


        Log.d("applog","refreshed");
    }


    @Override
    public void itemCategoryChanged(ItemCategory currentCategory) {

        notifiedCurrentCategory = currentCategory;

//        dataset.clear();
//        makeRequestRetrofit();

        onRefresh();
    }



    void notifyTitleChanged()
    {
        if(notifyPagerAdapter!=null)
        {
            notifyPagerAdapter.NotifyTitleChanged("Items (" + String.valueOf(dataset.size()) + "/" + String.valueOf(item_count) + ")",1);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);

        outState.putParcelable("currentCategory",notifiedCurrentCategory);

        outState.putParcelableArrayList("dataset",dataset);

    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Icepick.restoreInstanceState(this,savedInstanceState);

        if(savedInstanceState!=null)
        {
            notifiedCurrentCategory = savedInstanceState.getParcelable("currentCategory");

            ArrayList<Item> tempCat = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempCat);

            notifyTitleChanged();

            listAdapter.notifyDataSetChanged();
        }

    }


    // add / remove shop Items Begins


    @OnClick(R.id.add_selected_bulk)
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



    @OnClick(R.id.remove_selected)
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
