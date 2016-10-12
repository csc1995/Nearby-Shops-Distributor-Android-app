package org.localareadelivery.distributorapp.ShopList;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Distributor;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.ModelEndpoints.ShopEndPoint;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopService;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;
import org.localareadelivery.distributorapp.Utility.UtilityLogin;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {



    // check whether the activity is running or not
    boolean isActivityRunning = false;

    private SwipeRefreshLayout swipeContainer;

//    @Inject ShopDAO shopDAO;

    @Inject
    ShopService shopService;

    ArrayList<Shop> dataset = new ArrayList<>();

    @Bind(R.id.shopsList)
    RecyclerView shopList;

    ShopsListAdapter shopListAdapter;
    GridLayoutManager layoutManager;

    @Bind(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        // components for dependency injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);



        setupSwipeRefreshLayout();
        setupRecyclerView();



        if(savedInstanceState == null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {

//                    Log.d("applog",UtilityGeneral.getServiceURL(ShopList.this));

                        offset = 0;
                        dataset.clear();
                        makeNetworkCall();

                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });
        }

    }


    private int limit = 30;
    @State int offset = 0;

    @State int item_count = 0;

    void setupRecyclerView()
    {

        shopListAdapter = new ShopsListAdapter(this,dataset,this);
        shopList.setAdapter(shopListAdapter);

        layoutManager = new GridLayoutManager(null,1);

        shopList.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);

        shopList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition() == dataset.size()-1)
                {
                    // trigger fetch next page


                    Log.d("applog","Shop List : trigger next fetch " + String.valueOf(offset) + " : " + String.valueOf(limit)
                    + " : " + String.valueOf(item_count));

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }

                }

            }
        });

    }


    void setupSwipeRefreshLayout()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
    }





    @OnClick(R.id.fab)
    public void fabClick(View view)
    {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          //      .setAction("Action", null).show();

        startActivity(new Intent(ShopList.this,AddShop.class));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the ShopList/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    ProgressBar progressBar;


    @Override
    protected void onPause() {
        super.onPause();

        isActivityRunning = false;


    }

    void notifyDelete()
    {

        offset = 0; // reset the offset
        dataset.clear();
        makeNetworkCall();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    void makeNetworkCall()
    {

        Log.d("applog","Shop List : trigger Network Call " + String.valueOf(offset) + " : " + String.valueOf(limit)
                + " : " + String.valueOf(item_count));

        Distributor distributor = UtilityLogin.getDistributor(this);

        Integer distributorID = null;

        if(distributor!=null)
        {
            distributorID = distributor.getDistributorID();
        }

        Call<ShopEndPoint> endPointCall = shopService.getShopEndpoint(distributorID,
                null,null,null,null,null,null,"shop_id",limit,offset,false);

        endPointCall.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {

                if (response.body() != null) {

                    dataset.addAll(response.body().getResults());

                    item_count = response.body().getItemCount();
                }


                shopListAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {


                showToastMessage("Network request failed. Please check your connection !");

                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

            }
        });


    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    // flag for preventing the progress bar showing when the list is refreshed.
    boolean isRefreshed = false;

    @Override
    public void onRefresh() {

        isRefreshed = true;

        offset = 0;
        dataset.clear();
        makeNetworkCall();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
        outState.putParcelableArrayList("dataset",dataset);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            ArrayList<Shop> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);

            shopListAdapter.notifyDataSetChanged();
        }
    }
}
