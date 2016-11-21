package org.localareadelivery.distributorapp.DeliveryGuyAccounts;

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
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDelivery;
import org.localareadelivery.distributorapp.DeliveryGuyAccounts.EditProfile.EditDeliveryFragment;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.DeliveryGuyDashboard;
import org.localareadelivery.distributorapp.DeliveryGuyInventory.DeliveryGuyInventory;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Interface.NotifyTitleChanged;
import org.localareadelivery.distributorapp.HomeDeliveryInventory.Interface.RefreshFragment;
import org.localareadelivery.distributorapp.ModelRoles.DeliveryGuySelf;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.localareadelivery.distributorapp.ShopHome.UtilityShopHome;
import org.localareadelivery.distributorapp.Utility.DividerItemDecoration;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 17/11/16.
 */


public class AccountsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Adapter.NotifyConfirmOrder {


    @Inject
    DeliveryGuySelfService deliveryGuySelfService;

    RecyclerView recyclerView;
    Adapter adapter;

    public List<DeliveryGuySelf> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


    private static final String ARG_ACCOUNTS_MODE = "arg_accounts_mode";
    public static final String MODE_ACCOUNTS_ENABLED = "accounts_enabled";
    public static final String MODE_ACCOUNTS_DISABLED = "accounts_disabled";


    public AccountsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    public final static String INTENT_REQUEST_CODE_KEY = "request_code_key";

    public final static int INTENT_CODE_DELIVERY_GUY_INVENTORY = 2;
    public final static int INTENT_CODE_DELIVERY_GUY_DASHBOARD = 3;

    int requestCode;




    public static AccountsFragment newInstance(boolean isEnabled) {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ACCOUNTS_MODE, isEnabled);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_guy_accounts, container, false);
        setRetainInstance(true);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();




        return rootView;
    }


    void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this,getContext());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL_LIST));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(1);
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


    @Override
    public void onRefresh() {
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



    void makeNetworkCall(final boolean clearDataset)
    {

        Shop currentShop = UtilityShopHome.getShop(getContext());


        Call<List<DeliveryGuySelf>> call
                = deliveryGuySelfService.getVehicles(UtilityLogin.getAuthorizationHeaders(getContext()),
                        currentShop.getShopID(),
                        getArguments().getBoolean(ARG_ACCOUNTS_MODE, true));


        call.enqueue(new Callback<List<DeliveryGuySelf>>() {
            @Override
            public void onResponse(Call<List<DeliveryGuySelf>> call, Response<List<DeliveryGuySelf>> response) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {
                    dataset.clear();
                }

                if(response.code()==200 && response.body()!=null)
                {
                    dataset.addAll(response.body());
                }

                notifyTitleChanged();
                adapter.notifyDataSetChanged();


                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<DeliveryGuySelf>> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

            }
        });

    }





    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }



    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            if(getArguments().getBoolean(ARG_ACCOUNTS_MODE,true))
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Enabled ( " + String.valueOf(dataset.size())+ " )",0);

            }
            else
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Disabled ( " + String.valueOf(dataset.size())+ " )",1);
            }


        }

    }


    // Refresh the Confirmed PlaceHolderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }


    void refreshConfirmedFragment()
    {
        Fragment fragment = getActivity().getSupportFragmentManager()
                .findFragmentByTag(makeFragmentName(R.id.container,1));

        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment)fragment).refreshFragment();
        }
    }

    @Override
    public void notifyEditClick(DeliveryGuySelf deliveryGuySelf) {

        Intent intent = new Intent(getContext(), EditDelivery.class);
        intent.putExtra(EditDeliveryFragment.DELIVERY_GUY_INTENT_KEY,deliveryGuySelf);
        intent.putExtra(EditDeliveryFragment.EDIT_MODE_INTENT_KEY,EditDeliveryFragment.MODE_UPDATE);
        startActivity(intent);
    }

    @Override
    public void notifyListItemClick(DeliveryGuySelf deliveryGuySelf) {

        requestCode = getActivity().getIntent().getIntExtra(INTENT_REQUEST_CODE_KEY,-1);

        if(requestCode == INTENT_CODE_DELIVERY_GUY_INVENTORY)
        {

            Intent vehicleDashboardIntent = new Intent(getContext(),DeliveryGuyInventory.class);
            vehicleDashboardIntent.putExtra(DeliveryGuyInventory.DELIVERY_VEHICLE_INTENT_KEY, deliveryGuySelf);
            startActivity(vehicleDashboardIntent);
        }
        else if(requestCode == INTENT_CODE_DELIVERY_GUY_DASHBOARD)
        {
            Intent vehicleDashboardIntent = new Intent(getContext(),DeliveryGuyDashboard.class);
            vehicleDashboardIntent.putExtra(DeliveryGuyDashboard.DELIVERY_VEHICLE_INTENT_KEY, deliveryGuySelf);
            startActivity(vehicleDashboardIntent);

        }

    }
}

