package org.localareadelivery.distributorapp.OrdersHomeDelivery;

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

/**
 * Created by sumeet on 13/6/16.
 */


/**
 * A placeholder fragment containing a simple view.
 */
public class ConfirmedOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<Order>>,AdapterConfirmedOrders.NotificationReciever {


    @Inject
    OrderService orderService;

    RecyclerView recyclerView;

    AdapterConfirmedOrders adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;



    NotificationReceiver notifyPagerAdapter;


    public ConfirmedOrdersFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConfirmedOrdersFragment newInstance() {
        ConfirmedOrdersFragment fragment = new ConfirmedOrdersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_confirmed_orders, container, false);


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

        adapter = new AdapterConfirmedOrders(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/400);

    }


    @Override
    public void notifyConfirmOrder(Order order) {


        order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);

        Call<ResponseBody> call = orderService.putOrder(order.getOrderID(),order);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Order Set Packed !");

                    makeNetworkCall();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });


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

            Call<List<Order>> call = orderService.getOrders(0, currentShop.getShopID(),false,
                                            OrderStatusHomeDelivery.ORDER_CONFIRMED,
                                            0,0,true,true);


            call.enqueue(this);

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }


    public NotificationReceiver getNotifyPagerAdapter() {
        return notifyPagerAdapter;
    }

    public void setNotifyPagerAdapter(NotificationReceiver notifyPagerAdapter) {
        this.notifyPagerAdapter = notifyPagerAdapter;
    }

    @Override
    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

        if(response.body()!= null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();

            if(notifyPagerAdapter!=null)
            {
                notifyPagerAdapter.notifyConfirmedOrdersChanged();
            }

        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();


            if(notifyPagerAdapter!=null)
            {
                notifyPagerAdapter.notifyConfirmedOrdersChanged();
            }
        }

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onFailure(Call<List<Order>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }


    public interface NotificationReceiver
    {
        void notifyConfirmedOrdersChanged();
    }


}
