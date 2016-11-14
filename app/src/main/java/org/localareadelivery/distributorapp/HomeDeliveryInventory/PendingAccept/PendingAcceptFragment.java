package org.localareadelivery.distributorapp.HomeDeliveryInventory.PendingAccept;

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

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelEndpoints.OrderEndPoint;
import org.localareadelivery.distributorapp.ModelStats.OrderStatusHomeDelivery;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.OrderService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendingAcceptFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<Order>>,AdapterPendingAccept.NotificationReciever {


    @Inject
    OrderService orderService;
    RecyclerView recyclerView;
    AdapterPendingAccept adapter;

    public List<Order> dataset = new ArrayList<>();
    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



    public PendingAcceptFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }



    public static PendingAcceptFragment newInstance() {
        PendingAcceptFragment fragment = new PendingAcceptFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_home_delivery_pending_accept, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        setupRecyclerView();

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

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

        adapter = new AdapterPendingAccept(dataset,getActivity(),this);

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

    }




    @Override
    public void onRefresh() {

        makeNetworkCall();
    }


    @Override
    public void onResume() {
        super.onResume();

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                adapter.notifyDataSetChanged();
            }
        });
    }



    void makeNetworkCall()
    {

            Shop currentShop = ApplicationState.getInstance().getCurrentShop();

            Call<OrderEndPoint> call = orderService.getOrders(null, currentShop.getShopID(),false,
                                            OrderStatusHomeDelivery.PENDING_HANDOVER,
                                            null,null,null,null,true,true,null,null,null,null);


//            call.enqueue(this);

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

        if(response.body()!= null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();


        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();
        }

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onFailure(Call<List<Order>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void notifyCancelHandover(Order order) {


        order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);
        order.setDeliveryVehicleSelfID(0);

        Call<ResponseBody> call = orderService.putOrder(order.getOrderID(),order);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Handover cancelled !");

                    makeNetworkCall();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });
    }

}
