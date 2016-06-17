package org.localareadelivery.distributorapp.VehicleInventory;

import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelStats.DeliveryVehicleSelf;
import org.localareadelivery.distributorapp.ModelStats.OrderStatusHomeDelivery;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.OrderService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
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
public class PaymentsPendingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<Order>>,AdapterPaymentsPending.NotificationReciever {


    @Inject
    OrderService orderService;

    RecyclerView recyclerView;

    AdapterPaymentsPending adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;



    NotificationReceiver notificationReceiver;

    DeliveryVehicleSelf deliveryVehicleSelf;
    TextView ordersTotal;

    TextView receivedTotal;


    public PaymentsPendingFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PaymentsPendingFragment newInstance() {
        PaymentsPendingFragment fragment = new PaymentsPendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_payments_pending_vd, container, false);
        ButterKnife.bind(this,rootView);

        ordersTotal = (TextView) rootView.findViewById(R.id.ordersTotal);
        receivedTotal = (TextView) rootView.findViewById(R.id.receivedTotal);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        setupRecyclerView();

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        setupSwipeContainer();



        if(savedInstanceState!=null)
        {
            // restore instance state
            deliveryVehicleSelf = savedInstanceState.getParcelable("savedVehicle");
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


    void setupRecyclerView()
    {

        adapter = new AdapterPaymentsPending(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/400);

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

        if(deliveryVehicleSelf==null)
        {
            return;
        }

        Shop currentShop = ApplicationState.getInstance().getCurrentShop();

            Call<List<Order>> call = orderService.getOrders(0, currentShop.getShopID(),false,
                                            OrderStatusHomeDelivery.HANDED_TO_END_USER,
                                            0,deliveryVehicleSelf.getID(),false,null,true,true);


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
            updateTotal();

            if(notificationReceiver !=null)
            {
                notificationReceiver.notifyPendingAcceptChanged();
            }

        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();
            updateTotal();


            if(notificationReceiver !=null)
            {
                notificationReceiver.notifyPendingAcceptChanged();
            }

        }

        swipeContainer.setRefreshing(false);

    }


    int totalLabel;


    void updateTotal()
    {

        int total = 0;

        if(dataset!=null)
        {
            for(Order order : dataset)
            {
                total = total + (order.getOrderStats().getItemTotal()+ order.getDeliveryCharges());
            }


            ordersTotal.setText("All Orders Total : "  + total + "\nCollect " + total + " from Delivery guy.");
            receivedTotal.setText("Received " + total + " from Delivery Guy.");

        }

        if(total == 0)
        {
            ordersTotal.setVisibility(View.GONE);
            receivedTotal.setVisibility(View.GONE);

        }else
        {
            ordersTotal.setVisibility(View.VISIBLE);
            receivedTotal.setVisibility(View.VISIBLE);
        }

        totalLabel = total;
    }




    @OnClick(R.id.receivedTotal)
    void receivedAllClick(View view)
    {

//        DialogFragment newFragment = new NotifyUserDialogFragment();
  //      newFragment.show(getActivity().getSupportFragmentManager(), "notice");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Payment Received !")
                .setMessage("Did you received " + String.valueOf(totalLabel) + " from Delivery Guy !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        updatePaymentReceived();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showToastMessage("Update Cancelled !");
                    }
                })
                .show();



    }



    void updatePaymentReceived()
    {
        for(Order order : dataset)
        {
            order.setPaymentReceived(true);
        }

        Call<ResponseBody> call = orderService.putOrderBulk(dataset);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Successful !");

                }else
                {
                    showToastMessage("Error while updating ! ");
                }

                makeNetworkCall();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request failed !");

            }
        });

    }







    @Override
    public void onFailure(Call<List<Order>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void notifyCancelHandover(Order order) {

        order.setPaymentReceived(true);

        Call<ResponseBody> call = orderService.putOrder(order.getOrderID(),order);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");

                    makeNetworkCall();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });
    }


    public DeliveryVehicleSelf getDeliveryVehicleSelf() {
        return deliveryVehicleSelf;
    }

    public void setDeliveryVehicleSelf(DeliveryVehicleSelf deliveryVehicleSelf) {
        this.deliveryVehicleSelf = deliveryVehicleSelf;
    }

    public interface NotificationReceiver
    {
        void notifyPendingAcceptChanged();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("savedVehicle",deliveryVehicleSelf);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
