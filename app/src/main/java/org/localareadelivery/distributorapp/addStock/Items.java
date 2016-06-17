package org.localareadelivery.distributorapp.addStock;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;
import org.localareadelivery.distributorapp.Utility.VolleySingleton;
import org.localareadelivery.distributorapp.addStock.DiscardedCode.ItemCategories;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Items extends AppCompatActivity {



    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";
    public static final String ADD_ITEM_INTENT_KEY = "addItemIntentKey";


    ArrayList<Item> dataset = new ArrayList<>();


    RecyclerView itemsList;
    ItemsAdapter itemsAdapter;

    TextView itemCategoryName;

    ItemCategory itemCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstock_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // setup recycler View
        itemsList = (RecyclerView) findViewById(R.id.recyclerViewItems);

        itemCategory = (ItemCategory) getIntent().getParcelableExtra(ItemCategories.ITEM_CATEGORY_INTENT_KEY);
        itemsAdapter = new ItemsAdapter(dataset,this,this,itemCategory,shopItemDataset);

        Log.d("applog","CategoryID : " + String.valueOf(itemCategory.getItemCategoryID()));

        itemsList.setAdapter(itemsAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        itemsList.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);

        //itemsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        itemCategoryName = (TextView) findViewById(R.id.categoryName);
        itemCategoryName.setText("// " + itemCategory.getCategoryName());
    }




    public void makeRequest()
    {

        String url = "";

        Shop shop = ApplicationState.getInstance().getCurrentShop();

        if(itemCategory!=null) {
            url = getServiceURL() + "/api/Item?ItemCategoryID=" + itemCategory.getItemCategoryID() + "&ShopID=" + shop.getShopID();
        }

        Log.d("response",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);

                parseJSON(response);

                itemsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void parseJSON(String jsonString)
    {
        try {

            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Item item = new Item();

                item.setItemID(jsonObject.getInt("itemID"));
                item.setItemDescription(jsonObject.getString("itemDescription"));
                item.setItemName(jsonObject.getString("itemName"));
                dataset.add(item);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




    void makeRetrofitRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ItemService itemService = retrofit.create(ItemService.class);


        Call<List<Item>> items = itemService.getItems(itemCategory.getItemCategoryID(),
                ApplicationState.getInstance().getCurrentShop().getShopID());

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
        //makeRetrofitRequest();
        makeRequest();
        makeShopItemRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataset.clear();
        //makeRetrofitRequest();
        makeRequest();
        makeShopItemRequest();

    }




    Map<Integer,ShopItem> shopItemDataset = new HashMap<>();

    void makeShopItemRequest()
    {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ShopItemService shopItemService = retrofit.create(ShopItemService.class);

        Call<List<ShopItem>> shopItemsCall =  shopItemService.getShopItems(
                ApplicationState.getInstance().getCurrentShop().getShopID(),
                0,itemCategory.getItemCategoryID());


        shopItemsCall.enqueue(new Callback<List<ShopItem>>() {
            @Override
            public void onResponse(Call<List<ShopItem>> call, retrofit2.Response<List<ShopItem>> response) {


                if(response.body()!=null) {

                    List<ShopItem> shopItemList = response.body();

                    if(shopItemList!=null)
                    {
                        for(ShopItem shopItem: shopItemList)
                        {
                            shopItemDataset.put(shopItem.getItemID(),shopItem);
                            itemsAdapter.notifyDataSetChanged();
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<List<ShopItem>> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onPause() {

        super.onPause();

    }
}
