package org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.OutForDelivery;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
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
import org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.Interfaces.NotifyLocation;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.Model.DeliveryAddress;
import org.nearbyshops.shopkeeperapp.Model.Order;
import org.nearbyshops.shopkeeperapp.ModelEndpoints.OrderEndPoint;
import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.SlidingLayerSort.UtilitySortOrdersHD;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.OrderServiceDeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.DeliveryGuyDashboard.DELIVERY_GUY_INTENT_KEY_DASHBOARD;

/**
 * Created by sumeet on 13/6/16.
 */


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentOutForDelivery extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterOutForDelivery.NotifyHandoverToUser,NotifyLocation, NotifySearch,NotifySort{


//    @Inject
//    OrderService orderService;



    Location locationResult;



    @Inject
    OrderServiceDeliveryGuySelf orderServiceDelivery;

    RecyclerView recyclerView;
    AdapterOutForDelivery adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;
    boolean isDestroyed;



//    NotificationReceiver notificationReceiver;

    DeliveryGuySelf deliveryGuySelf;


    public FragmentOutForDelivery() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentOutForDelivery newInstance() {
        FragmentOutForDelivery fragment = new FragmentOutForDelivery();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_pending_handover_driver_dashboard, container, false);

        setRetainInstance(true);

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        deliveryGuySelf = getActivity().getIntent().getParcelableExtra(DELIVERY_GUY_INTENT_KEY_DASHBOARD);

//
//        if(savedInstanceState!=null)
//        {
//            // restore instance state
//            deliveryGuySelf = savedInstanceState.getParcelable("savedVehicle");
//
//        }
        if(savedInstanceState==null)
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

        adapter = new AdapterOutForDelivery(dataset,this);

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

                                makeNetworkCall(false,false);
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

        makeNetworkCall(true,true);
        isDestroyed=false;
    }


    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
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



    void makeNetworkCall(final boolean clearDataset, boolean resetOffset)
    {
        if(resetOffset)
        {
            offset = 0;
        }

//        if(deliveryGuySelf ==null)
//        {
//            return;
//        }

//        Shop currentShop = UtilityShopHome.getShop(getContext());



//        Call<OrderEndPoint> call = orderService
//                .getOrders(null, deliveryGuySelf.getShopID(),false,
//                        OrderStatusHomeDelivery.HANDOVER_ACCEPTED,
//                        null, deliveryGuySelf.getDeliveryGuyID(),null,null,true,true,
//                        null,limit,offset,null);


        int deliveryGuyID = 0;

        if(deliveryGuySelf!=null)
        {
            deliveryGuyID = deliveryGuySelf.getDeliveryGuyID();
        }

        double lat = 0;
        double lon = 0;

        if(locationResult!=null)
        {
            lat = locationResult.getLatitude();
            lon = locationResult.getLongitude();
        }

        String current_sort = "";
        current_sort = UtilitySortOrdersHD.getSort(getContext()) + " " + UtilitySortOrdersHD.getAscending(getContext());


        Call<OrderEndPoint> call = orderServiceDelivery
                .getOrders(UtilityLogin.getAuthorizationHeaders(getActivity()),
                        deliveryGuyID,
                        null,null,false,OrderStatusHomeDelivery.HANDOVER_ACCEPTED,
                        null,
                        null,null,
                        lat,lon,
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
    public void notifyHandoverToUser(Order order) {


//        order.setStatusHomeDelivery(OrderStatusHomeDelivery.PENDING_DELIVERY);

        Call<ResponseBody> call = orderServiceDelivery.handoverToUser(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
                    makeRefreshNetworkCall();
                }
                else if(response.code()==304)
                {
                    showToastMessage("Not Successful !");
                }
                else if(response.code()==401 || response.code()==404)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });
    }

    @Override
    public void notifyReturnPackage(Order order) {

        Call<ResponseBody> callReturn = orderServiceDelivery.returnOrderPackage(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID());

        callReturn.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Successful !");
                    makeRefreshNetworkCall();
                }
                else if(response.code()==304)
                {
                    showToastMessage("Not Updated !");
                }
                else if(response.code()==401 || response.code()==404)
                {
                    showToastMessage("Not Permitted !");
                }
                else
                {
                    showToastMessage("Server Error !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");
            }
        });

    }

    @Override
    public void notifyGetDirections(DeliveryAddress deliveryAddress) {

//        showToastMessage("Get Directions : " + String.valueOf(deliveryAddress.getLatitude())+ ":" + String.valueOf(deliveryAddress.getLongitude()));

        String str_latitude = String.valueOf(deliveryAddress.getLatitude());
        String str_longitude = String.valueOf(deliveryAddress.getLongitude());

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_latitude +  "," + str_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }


//    public DeliveryGuySelf getDeliveryGuySelf() {
//        return deliveryGuySelf;
//    }
//
//    public void setDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf) {
//        this.deliveryGuySelf = deliveryGuySelf;
//    }
//
//
//
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
                            "Out For Delivery (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",1);


        }
    }




    @Override
    public void fetchedLocation(Location location) {
        locationResult = location;
        makeRefreshNetworkCall();
        showToastMessage("Location Updated !");
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
