package org.localareadelivery.distributorapp.HomeDeliveryInventory.Packed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderEndPoint;
import org.localareadelivery.distributorapp.ModelStatusCodes.OrderStatusHomeDelivery;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.ConfirmItemsForDelivery;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Interface.ConfirmOrdersClicked;
import org.localareadelivery.distributorapp.CommonInterfaces.NotifyTitleChanged;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Interface.RefreshFragment;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.OrderServiceShopStaff;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
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
public class PackedOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ConfirmOrdersClicked, RefreshFragment, AdapterPackedOrders.NotifyCancelHandover{

    @Inject
    OrderServiceShopStaff orderServiceShopStaff;

//    @Inject
//    OrderService orderService;



    RecyclerView recyclerView;
    AdapterPackedOrders adapter;
    public List<Order> dataset = new ArrayList<>();
    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

//    @Bind(R.id.confirmItems)
//    TextView confirmItems;



    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;

    boolean isDestroyed;



    public PackedOrdersFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PackedOrdersFragment newInstance() {
        PackedOrdersFragment fragment = new PackedOrdersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_packed_orders, container, false);

        setRetainInstance(true);

        ButterKnife.bind(this,rootView);

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);


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

        adapter = new AdapterPackedOrders(dataset,this);

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


                if(offset + limit > layoutManager.findLastVisibleItemPosition())
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


    void onClickConfirmItems()
    {


        if(adapter.getSelectedOrders().size()==0)
        {
            showToastMessage("No Orders Selected !");

            return;
        }


        ArrayList<Order> selected = new ArrayList<>();

        for (Map.Entry<Integer, Order> entry : adapter.getSelectedOrders().entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());

            selected.add(entry.getValue());

        }

        //intent.putExtra("selected",selected);

        ApplicationState.getInstance().getSelectedOrdersForDelivery().clear();
        ApplicationState.getInstance().getSelectedOrdersForDelivery().addAll(selected);


        Intent intent = new Intent(getActivity(), ConfirmItemsForDelivery.class);
        getActivity().startActivity(intent);
    }



    @Override
    public void onRefresh() {

        offset = 0; // reset the offset
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
    }



    void makeNetworkCall(final boolean clearDataset)
    {

//            Shop currentShop = UtilityShopHome.getShop(getContext());
//
//            Call<OrderEndPoint> call = orderService.getOrders(null, currentShop.getShopID(),false,
//                                            OrderStatusHomeDelivery.ORDER_PACKED,
//                                            null,null,null,null,true,true,
//                    null, limit,offset,null);



        Call<OrderEndPoint> call = orderServiceShopStaff.getOrders(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                null,null,false,
                OrderStatusHomeDelivery.ORDER_PACKED,null,null,
                null,null,
                null,null,
                null,
                null,limit,offset,null);


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



    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged(
                            "Packed ( " + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + " )",2);


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isDestroyed = true;
        ButterKnife.unbind(this);
    }

    @Override
    public void confirmOrdersClicked() {
        onClickConfirmItems();
    }



    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }




    @Override
    public void notifyCancelOrder(final Order order) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Cancel Order !")
                .setMessage("Are you sure you want to cancel this order !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cancelOrder(order);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage(" Not Cancelled !");
                    }
                })
                .show();
    }


    private void cancelOrder(Order order) {

//        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());
        Call<ResponseBody> call = orderServiceShopStaff.cancelledByShop(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID()
        );



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200 )
                {
                    showToastMessage("Successful");
                    makeRefreshNetworkCall();
                }
                else if(response.code() == 304)
                {
                    showToastMessage("Not Cancelled !");
                }
                else
                {
                    showToastMessage("Server Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Check your internet connection !");
            }
        });

    }


}
