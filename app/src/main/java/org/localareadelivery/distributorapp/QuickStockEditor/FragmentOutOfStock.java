package org.localareadelivery.distributorapp.QuickStockEditor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopItemEndPoint;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.Icepick;
import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 13/6/16.
 */


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentOutOfStock extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterOutOfStock.NotificationReceiver, Callback<ShopItemEndPoint> {




    @Inject
    ShopItemService shopItemService;

    RecyclerView recyclerView;

    AdapterOutOfStock adapter;

    public ArrayList<ShopItem> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;


    NotificationReceiverPager notificationReceiverPager;


    public static String ARG_MODE_KEY = "mode_key";

    public static int MODE_OUT_OF_STOCK = 1;
    public static int MODE_LOW_STOCK = 2;
    public static int MODE_RECENTLY_UPDATED = 3;
    public static int MODE_RECENTLY_ADDED = 4;
    public static int MODE_PRICE_NOT_SET = 5;



    public FragmentOutOfStock() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentOutOfStock newInstance(int mode) {
        FragmentOutOfStock fragment = new FragmentOutOfStock();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE_KEY,mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quick_stock_out_of_stock, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        setupRecyclerView();

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        setupSwipeContainer();


        if(savedInstanceState == null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {


                        offset = 0; // reset the offset
                        dataset.clear();
                        makeNetworkCall();

                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });
        }


        return rootView;
    }


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



    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;

    void setupRecyclerView()
    {

        adapter = new AdapterOutOfStock(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }

                }
            }
        });

    }



    @Override
    public void onRefresh() {

        dataset.clear();
        offset=0; // reset the offset
        makeNetworkCall();
    }




    void makeNetworkCall() {
        int mode = getArguments().getInt(ARG_MODE_KEY);

        Shop currentShop = ApplicationState.getInstance().getCurrentShop();

        Call<ShopItemEndPoint> call = null;


        if (mode == MODE_OUT_OF_STOCK) {

//            call = shopItemService.getShopItems(currentShop.getShopID(), null, null, true, null);

            call = shopItemService.getShopItemEndpoint(null,
                    currentShop.getShopID(),null,null,null,
                    null,null,null,null,null,true,null,null,null,"item_id",limit,offset,false);


        } else if (mode == MODE_LOW_STOCK)
        {

//            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"available_item_quantity",null,null);

            call = shopItemService.getShopItemEndpoint(null,
                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
                    null,null,null,"available_item_quantity",limit,offset,false);

        }
        else if (mode == MODE_RECENTLY_ADDED)
        {
//            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"date_time_added desc",null,null);


            call = shopItemService.getShopItemEndpoint(null,
                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
                    null,null,null,"date_time_added desc",limit,offset,false);


        }else if (mode == MODE_RECENTLY_UPDATED)
        {
//            call = shopItemService.getShopItems(currentShop.getShopID(),null,null,null,null,"last_update_date_time desc",null,null);

            call = shopItemService.getShopItemEndpoint(null,
                    currentShop.getShopID(),null,null,null,null,null,null,null,null,null,
                    null,null,null,"last_update_date_time desc",limit,offset,false);
        }
        else if (mode == MODE_PRICE_NOT_SET)
        {
            call = shopItemService.getShopItemEndpoint(null,
                    currentShop.getShopID(),null,null,null,
                    null,null,null,null,null,null,true,null,null,"item_id",limit,offset,false);
        }


        if(call!=null)
        {
            call.enqueue(this);
        }

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public void notifyShopItemUpdated(final ShopItem shopItem) {


        Call<ResponseBody> call = shopItemService.putShopItem(shopItem);

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

        Call<ResponseBody> responseBodyCall = shopItemService.deleteShopItem(shopItem.getShopID(),shopItem.getItemID());

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

                }else if(response.code() == 304) {

                    showToastMessage("Not removed !");

                }
                else
                {
                    showToastMessage("Server Error !");
                }



                dataset.clear();
                offset = 0;
                makeNetworkCall();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                showToastMessage("Network request failed !");

            }
        });


    }



    @Override
    public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


        if(response.body()!= null)
        {
            dataset.addAll(response.body().getResults());
            item_count = response.body().getItemCount();
        }

        if(notificationReceiverPager!=null)
        {
            notificationReceiverPager.OutOfStockChanged();
        }

        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }


    interface NotificationReceiverPager
    {
        void OutOfStockChanged();
    }



    // save and restore instance state



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        Icepick.saveInstanceState(this, outState);


        outState.putParcelableArrayList("dataset",dataset);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        Icepick.restoreInstanceState(this, savedInstanceState);

        if (savedInstanceState != null) {

            ArrayList<ShopItem> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);
            adapter.notifyDataSetChanged();
        }

    }


}
