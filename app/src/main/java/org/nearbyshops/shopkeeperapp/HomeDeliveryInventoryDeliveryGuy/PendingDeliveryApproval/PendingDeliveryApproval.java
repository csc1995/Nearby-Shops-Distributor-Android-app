package org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.PendingDeliveryApproval;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.CommonInterfaces.NotifyTitleChanged;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.Model.Order;
import org.nearbyshops.shopkeeperapp.ModelEndpoints.OrderEndPoint;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.SlidingLayerSort.UtilitySortOrdersHD;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderServiceShopStaff;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.nearbyshops.shopkeeperapp.HomeDeliveryInventoryDeliveryGuy.DeliveryGuyInventory.DELIVERY_VEHICLE_INTENT_KEY;

/**
 * Created by sumeet on 13/6/16.
 */


public class PendingDeliveryApproval extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,AdapterDeliveryApproval.NotifyMarkDelivered, NotifySearch,NotifySort{


//    @Inject
//    OrderService orderService;

    @Inject
    OrderServiceShopStaff orderServiceShopStaff;

    RecyclerView recyclerView;
    AdapterDeliveryApproval adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



//    NotificationReceiver notificationReceiver;

    DeliveryGuySelf deliveryGuySelf;



    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;
    boolean isDestroyed;




    public PendingDeliveryApproval() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PendingDeliveryApproval newInstance() {
        PendingDeliveryApproval fragment = new PendingDeliveryApproval();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_delivery_approval_dgi, container, false);


        setRetainInstance(true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(deliveryGuySelf==null)
        {
            deliveryGuySelf = getActivity().getIntent().getParcelableExtra(DELIVERY_VEHICLE_INTENT_KEY);
        }




//        if(savedInstanceState!=null)
//        {
//             restore instance state
//            deliveryGuySelf = savedInstanceState.getParcelable("savedVehicle");
//        }
        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }



        setupRecyclerView();
        setupSwipeContainer();

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


    void setupRecyclerView()
    {

        adapter = new AdapterDeliveryApproval(dataset,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1)
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }



                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;


                        swipeContainer.post(new Runnable() {
                            @Override
                            public void run() {

                                swipeContainer.setRefreshing(true);

                                makeNetworkCall(false);
                            }
                        });

                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }


//    int previous_position = -1;

    @Override
    public void onRefresh() {
        offset = 0;
        makeNetworkCall(true);
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
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed= false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }


    void makeNetworkCall(final boolean clearDataset)
    {

        if(deliveryGuySelf ==null)
        {
            return;
        }

//        Shop currentShop = UtilityShopHome.getShop(getContext());

//            Call<OrderEndPoint> call = orderService
//                    .getOrders(null, currentShop.getShopID(),false,
//                            OrderStatusHomeDelivery.PENDING_DELIVERY,
//                            null, deliveryGuySelf.getDeliveryGuyID(),
//                            null,false,
//                            true,true,
//                            null,limit,offset,null);

        String current_sort = "";
        current_sort = UtilitySortOrdersHD.getSort(getContext()) + " " + UtilitySortOrdersHD.getAscending(getContext());


        Call<OrderEndPoint> call = orderServiceShopStaff.getOrders(
                    UtilityLogin.getAuthorizationHeaders(getActivity()),
                    null,null,false,
                    OrderStatusHomeDelivery.PENDING_DELIVERY,null,deliveryGuySelf.getDeliveryGuyID(),
                    null,false,
                    null,null,
                    null,
                    searchQuery,current_sort,limit,offset,null);



            call.enqueue(new Callback<OrderEndPoint>() {
                @Override
                public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!= null)
                    {
                        item_count = response.body().getItemCount();

                        if(clearDataset)
                        {
                            dataset.clear();
                        }

                        dataset.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        notifyTitleChanged();

                    }

                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<OrderEndPoint> call, Throwable t) {
                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage("Network Request failed !");
                    swipeContainer.setRefreshing(false);
                }
            });

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void notifyMarkDelivered(Order order, final int position) {


//        order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);
//        order.setDeliveryGuySelfID(null);

//        Call<ResponseBody> call = orderService.putOrder(order.getOrderID(),order);

        Call<ResponseBody> call = orderServiceShopStaff.markDelivered(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Handover cancelled !");
//                    makeRefreshNetworkCall();
                    dataset.remove(position);
                    adapter.notifyItemRemoved(position);
                    item_count = item_count-1;
                    notifyTitleChanged();
                }
                else
                {
                    showToastMessage("Failed code : " + String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });
    }
//
//
//    public DeliveryGuySelf getDeliveryGuySelf() {
//        return deliveryGuySelf;
//    }
//
//    public void setDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf) {
//        this.deliveryGuySelf = deliveryGuySelf;
//    }

    /*public interface NotificationReceiver
    {
        void notifyPendingAcceptChanged();
    }
    */

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("savedVehicle", deliveryGuySelf);
//    }





    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged(
                            "Pending Delivery Approval (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",2);


        }
    }




    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }

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



}
