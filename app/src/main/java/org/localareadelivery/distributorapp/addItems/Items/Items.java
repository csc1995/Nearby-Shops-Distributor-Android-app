package org.localareadelivery.distributorapp.addItems.Items;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;


import org.localareadelivery.distributorapp.DividerItemDecoration;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContractRetrofit.ItemService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Items extends AppCompatActivity {


    ArrayList<Item> dataset = new ArrayList<>();

    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";
    public static final String ADD_ITEM_INTENT_KEY = "addItemIntentKey";


    RecyclerView itemsList;
    ItemsAdapter itemsAdapter;

    @Bind(R.id.categoryName) TextView itemCategoryName;

    ItemCategory itemCategory;


    @Bind(R.id.fab) FloatingActionButton fab;


    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_items);

        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategory = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);


        // setup recycler View
        itemsList = (RecyclerView) findViewById(R.id.recyclerViewItems);


        if(itemCategory!=null) {

            itemsAdapter = new ItemsAdapter(dataset, this, this, itemCategory);

        }

        itemsList.setAdapter(itemsAdapter);


        gridLayoutManager = new GridLayoutManager(this,1);

        itemsList.setLayoutManager(gridLayoutManager);
        itemsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        if(itemCategory!=null) {
            itemCategoryName.setText("// " + itemCategory.getCategoryName());
        }



        // for setting the span count dynamically according to the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        gridLayoutManager.setSpanCount(metrics.widthPixels/350);
    }



    @OnClick(R.id.fab)
    void fabClick()
    {
        Intent addItemIntent = new Intent(this,AddItem.class);

        addItemIntent.putExtra(ADD_ITEM_INTENT_KEY,itemCategory);

        startActivity(addItemIntent);
    }



    public void makeRetrofitRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemService itemService = retrofit.create(ItemService.class);

        Call<List<Item>> items = itemService.getItems(itemCategory.getItemCategoryID());

        items.enqueue(new Callback<List<Item>>() {

            @Override
            public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {


                if(response.body()!=null) {

                    dataset.addAll(response.body());
                    itemsAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public void notifyDelete()
    {
        dataset.clear();
        makeRetrofitRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataset.clear();
        makeRetrofitRequest();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }
}
