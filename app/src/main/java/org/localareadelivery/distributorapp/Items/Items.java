package org.localareadelivery.distributorapp.Items;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DividerItemDecoration;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopItemService;
import org.localareadelivery.distributorapp.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Items extends AppCompatActivity {





    ArrayList<Item> dataset = new ArrayList<>();

    public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";
    public static final String ITEM_CATEGORY_NAME_KEY="itemCategoryNameKey";

    RecyclerView itemsList;
    ItemsAdapter itemsAdapter;

    TextView itemCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();

                Intent intent = new Intent(Items.this,AddItem.class);
                intent.putExtra(AddItem.ITEM_CATEGORY_ID_KEY,getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0));
                startActivity(intent);

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // setup recycler View
        itemsList = (RecyclerView) findViewById(R.id.recyclerViewItems);
        itemsAdapter = new ItemsAdapter(dataset,this,this,getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0));
        itemsList.setAdapter(itemsAdapter);
        itemsList.setLayoutManager(new GridLayoutManager(this,1));
        itemsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));


        itemCategoryName = (TextView) findViewById(R.id.categoryName);
        itemCategoryName.setText("// " + getIntent().getStringExtra(ITEM_CATEGORY_NAME_KEY));

        //makeRequest();


    }


    public void makeRequest()
    {
        String url = getServiceURL() + "/api/Item?ItemCategoryID=" + String.valueOf(getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0));

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
                item.setBrandName(jsonObject.getString("brandName"));
                item.setItemName(jsonObject.getString("itemName"));
                dataset.add(item);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
        makeRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dataset.clear();
        makeRequest();

    }






}
