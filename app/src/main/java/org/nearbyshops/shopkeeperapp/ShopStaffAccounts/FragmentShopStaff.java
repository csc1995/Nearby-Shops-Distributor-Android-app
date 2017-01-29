package org.nearbyshops.shopkeeperapp.ShopStaffAccounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.nearbyshops.shopkeeperapp.DaggerComponentBuilder;
import org.nearbyshops.shopkeeperapp.CommonInterfaces.NotifyTitleChanged;
import org.nearbyshops.shopkeeperapp.ModelRoles.ShopStaff;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ShopStaffService;
import org.nearbyshops.shopkeeperapp.ShopStaffAccounts.EditShopStaff.EditStaff;
import org.nearbyshops.shopkeeperapp.ShopStaffAccounts.EditShopStaff.EditStaffFragment;
import org.nearbyshops.shopkeeperapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 24/11/16.
 */


public class FragmentShopStaff extends Fragment implements Adapter.NotifyConfirmOrder, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ShopStaffService staffService;

    RecyclerView recyclerView;
    Adapter adapter;

    public List<ShopStaff> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


//    private static final String ARG_ACCOUNTS_MODE = "arg_accounts_mode";
//    public static final String MODE_ACCOUNTS_ENABLED = "accounts_enabled";
//    public static final String MODE_ACCOUNTS_DISABLED = "accounts_disabled";


    private static final String ARG_ENABLED = "section_number";

    public FragmentShopStaff() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static FragmentShopStaff newInstance(boolean enabled) {
        FragmentShopStaff fragment = new FragmentShopStaff();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ENABLED, enabled);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shop_staff, container, false);
        setRetainInstance(true);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();





//        View rootView = inflater.inflate(R.layout.fragment_shop_staff, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_ENABLED)));
        return rootView;
    }



    void setupRecyclerView()
    {

        adapter = new Adapter(dataset,this,getContext());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecorationCustom(getContext(),DividerItemDecorationCustom.HORIZONTAL_LIST));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);
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

//        Shop currentShop = UtilityShopHome.getShop(getContext());


        Call<List<ShopStaff>> call = staffService.getShopStaff(UtilityLogin.getAuthorizationHeaders(getContext()),
                getArguments().getBoolean(ARG_ENABLED));

        call.enqueue(new Callback<List<ShopStaff>>() {
            @Override
            public void onResponse(Call<List<ShopStaff>> call, Response<List<ShopStaff>> response) {

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


//                notifyRefresh();

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<ShopStaff>> call, Throwable t) {


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
            if(getArguments().getBoolean(ARG_ENABLED,true))
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Enabled (" + String.valueOf(dataset.size())+ ")",0);

            }
            else
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Disabled (" + String.valueOf(dataset.size())+ ")",1);
            }


        }

    }


    // Refresh the Confirmed PlaceHolderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }


    @Override
    public void notifyEditClick(ShopStaff staff) {

        Intent intent = new Intent(getContext(), EditStaff.class);
        UtilityShopStaff.saveShopStaff(staff,getContext());

        intent.putExtra(EditStaffFragment.EDIT_MODE_INTENT_KEY, EditStaffFragment.MODE_UPDATE);
        startActivity(intent);

        showToastMessage("Edit Click!");

    }

    @Override
    public void notifyListItemClick(ShopStaff staff) {

    }

}
