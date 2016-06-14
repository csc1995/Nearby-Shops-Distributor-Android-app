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

import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements ShopDAO.ReadShopCallback, SwipeRefreshLayout.OnRefreshListener {



    // check whether the activity is running or not
    boolean isActivityRunning = false;

    private SwipeRefreshLayout swipeContainer;

    @Inject ShopDAO shopDAO;

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
                .getDaoComponent().Inject(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }



        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //shopList = (RecyclerView) findViewById(R.id.shopsList);

        shopListAdapter = new ShopsListAdapter(this,dataset,this);
        shopList.setAdapter(shopListAdapter);



        layoutManager = new GridLayoutManager(null,1);

        shopList.setLayoutManager(layoutManager);
        //shopList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);
    }


    @OnClick(R.id.fab)
    public void fabClick(View view)
    {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          //      .setAction("Action", null).show();

        startActivity(new Intent(Home.this,AddShop.class));
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
        // automatically handle clicks on the Home/Up button, so long
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
    protected void onResume() {
        super.onResume();

        isActivityRunning = true;

        dataset.clear();

        if(!isRefreshed)
        {
            //progressBar.setVisibility(View.VISIBLE);
        }

        // reset the flag
        isRefreshed = false;

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    Log.d("applog",UtilityGeneral.getServiceURL(Home.this));
                    shopDAO.readShops(UtilityGeneral.getDistributorID(Home.this), Home.this);

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                shopListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        isActivityRunning = false;


    }

    void notifyDelete()
    {
        dataset.clear();

        shopDAO
                .readShops(
                        UtilityGeneral.getDistributorID(this),
                        this
                );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }



    @Override
    public void readShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, Shop shop) {

    }

    @Override
    public void readShopsCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<Shop> shops) {


        if (!isOffline) {

            if (isSuccessful) {

                dataset.clear();


                if (shops != null) {
                    dataset.addAll(shops);
                }


                progressBar.setVisibility(View.GONE);

                shopListAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);

            }
            else
            {
                // failed case
                progressBar.setVisibility(View.GONE);
                shopListAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);

            }


        }
        else
        {
            if(!isSuccessful)
            {
                //Toast.makeText(this,"Application is offline.No Data !",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                swipeContainer.setRefreshing(false);



            }
        }


    }



    // flag for preventing the progress bar showing when the list is refreshed.
    boolean isRefreshed = false;

    @Override
    public void onRefresh() {

        isRefreshed = true;

        dataset.clear();

        shopDAO
                .readShops(
                        UtilityGeneral
                                .getDistributorID(this),
                        this
                );

        shopListAdapter.notifyDataSetChanged();
    }




}
