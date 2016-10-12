package org.localareadelivery.distributorapp.OrdersHomeDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 13/6/16.
 */


/**
 * A placeholder fragment containing a simple view.
 */
public class PackedOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<Order>>, AdapterPackedOrders.NotificationReciever{


    @Inject
    OrderService orderService;

    RecyclerView recyclerView;

    AdapterPackedOrders adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

    @Bind(R.id.confirmItems)
    TextView confirmItems;


    NotificationReceiver notificationReceiver;


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

        ButterKnife.bind(this,rootView);


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

        adapter = new AdapterPackedOrders(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/400);

    }


    @OnClick(R.id.confirmItems)
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
                                            OrderStatusHomeDelivery.ORDER_PACKED,
                                            0,0,null,null,true,true);


            call.enqueue(this);

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }


    public NotificationReceiver getNotificationReceiver() {
        return notificationReceiver;
    }

    public void setNotificationReceiver(NotificationReceiver notificationReceiver) {
        this.notificationReceiver = notificationReceiver;
    }

    @Override
    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

        if(response.body()!= null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();

            if(notificationReceiver!=null)
            {
                notificationReceiver.notifyPackedOrdersChanged();
            }

        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();


            if(notificationReceiver!=null)
            {
                notificationReceiver.notifyPackedOrdersChanged();
            }
        }

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onFailure(Call<List<Order>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void notifyConfirmOrder(Order order) {

    }


    public interface NotificationReceiver
    {
        void notifyPackedOrdersChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
