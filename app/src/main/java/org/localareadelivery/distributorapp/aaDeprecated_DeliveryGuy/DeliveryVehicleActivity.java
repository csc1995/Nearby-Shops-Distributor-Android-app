package org.localareadelivery.distributorapp.aaDeprecated_DeliveryGuy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.DeliveryGuyDashboard.DeliveryGuyDashboard;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelRoles.DeliveryGuySelf;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.DeliveryGuySelfService;
import org.localareadelivery.distributorapp.HomeDeliveryInventoryDeliveryGuy.DeliveryGuyInventory;
import org.localareadelivery.distributorapp.ShopHome.UtilityShopHome;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryVehicleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, Adapter.NotificationReceiver, Callback<List<DeliveryGuySelf>> {

    @Inject
    DeliveryGuySelfService deliveryGuySelfService;

    RecyclerView recyclerView;

    Adapter adapter;

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

    List<DeliveryGuySelf> dataset = new ArrayList<>();


    Shop shop = null;


    public final static String INTENT_REQUEST_CODE_KEY = "request_code_key";

    public final static int INTENT_CODE_SELECT_VEHICLE = 1;
    public final static int INTENT_CODE_DASHBOARD = 2;
    public final static int INTENT_CODE_VEHICLE_DRIVER_DASHBOARD = 3;

    int requestCode;

    TextView addNewAddress;




    public DeliveryVehicleActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_vehicle_self);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // findView By id'// STOPSHIP: 11/6/16

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addNewAddress = (TextView) findViewById(R.id.addNewAddress);

        addNewAddress.setOnClickListener(this);

        requestCode = getIntent().getIntExtra(INTENT_REQUEST_CODE_KEY,0);

        setupSwipeContainer();
        setupRecyclerView();
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


        adapter = new Adapter(dataset,this,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this,1);

        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(
        //        new DividerItemDecorationCustom(this,DividerItemDecorationCustom.VERTICAL_LIST)
        //);

        //recyclerView.addItemDecoration(new DividerItemDecorationCustom(this,DividerItemDecorationCustom.HORIZONTAL_LIST));

        //itemCategoriesList.addItemDecoration(new DividerItemDecorationCustom(this,DividerItemDecorationCustom.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        int spanCount = (metrics.widthPixels/350);

//        if(spanCount > 0)
//        {
//            layoutManager.setSpanCount(spanCount);
//        }




        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }



    void makeNetworkCall()
    {

        Shop shop = UtilityShopHome.getShop(this);

        Call<List<DeliveryGuySelf>> call = deliveryGuySelfService
                .getVehicles(
                        UtilityLogin.getAuthorizationHeaders(this),
                        shop.getShopID(),null
                );

        call.enqueue(this);

    }



    @Override
    protected void onResume() {
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





    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.addNewAddress:

                addNewAddressClick(v);

                break;

            default:
                break;
        }

    }




    void addNewAddressClick(View view)
    {

        Intent intent = new Intent(this,AddVehicleSelfActivity.class);
        startActivity(intent);

    }

    @Override
    public void notifyEdit(DeliveryGuySelf deliveryGuySelf) {

        Intent intent = new Intent(this, EditAddressActivity.class);
        intent.putExtra(EditAddressActivity.DELIVERY_VEHICLE_SELF_INTENT_KEY, deliveryGuySelf);
        startActivity(intent);

    }

    @Override
    public void notifyRemove(DeliveryGuySelf deliveryGuySelf) {

        showToastMessage("Remove");

    }

    @Override
    public void notifyListItemClick(DeliveryGuySelf deliveryGuySelf) {

        requestCode = getIntent().getIntExtra(INTENT_REQUEST_CODE_KEY,0);

        if(requestCode == INTENT_CODE_SELECT_VEHICLE)
        {

            Intent output = new Intent();
            output.putExtra("output", deliveryGuySelf);
            setResult(2,output);
            finish();
        }
        else if(requestCode == INTENT_CODE_DASHBOARD)
        {

            Intent vehicleDashboardIntent = new Intent(this,DeliveryGuyInventory.class);
            vehicleDashboardIntent.putExtra(DeliveryGuyInventory.DELIVERY_VEHICLE_INTENT_KEY, deliveryGuySelf);
            startActivity(vehicleDashboardIntent);

        }else if(requestCode == INTENT_CODE_VEHICLE_DRIVER_DASHBOARD)
        {

            Intent vehicleDashboardIntent = new Intent(this,DeliveryGuyDashboard.class);
            vehicleDashboardIntent.putExtra(DeliveryGuyDashboard.DELIVERY_VEHICLE_INTENT_KEY, deliveryGuySelf);
            startActivity(vehicleDashboardIntent);
        }




    }

    @Override
    public void onResponse(Call<List<DeliveryGuySelf>> call, Response<List<DeliveryGuySelf>> response) {


        if(response.body()!=null)
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
    public void onFailure(Call<List<DeliveryGuySelf>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);

    }

}
