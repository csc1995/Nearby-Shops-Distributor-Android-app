package org.localareadelivery.distributorapp.addStock.DiscardedCode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.UtilityMethods.DividerItemDecoration;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

import java.util.ArrayList;

public class ItemCategories extends AppCompatActivity {

    ArrayList<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    ItemCategoriesAdapter listAdapter;
    RecyclerView.LayoutManager layoutManager;


    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_addstock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = (RecyclerView) findViewById(R.id.recyclerViewItemCategories);
        listAdapter = new ItemCategoriesAdapter(dataset,this,this);

        itemCategoriesList.setAdapter(listAdapter);
        layoutManager = new GridLayoutManager(null,1);

        itemCategoriesList.setLayoutManager(layoutManager);
        itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

    }



    public void makeRequest()
    {

        int shopID = ApplicationState.getInstance().getCurrentShop().getShopID();

        String url = getServiceURL() + "/api/ItemCategory?ShopID=" + shopID;

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);
                parseJSON(response);
                listAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }




    public void parseJSON(String jsonString)
    {
        try {

            JSONArray array = new JSONArray(jsonString);

            for(int i=0;i<array.length();i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ItemCategory itemCategory = new ItemCategory();
                itemCategory.setItemCategoryID(jsonObject.getInt("itemCategoryID"));
                itemCategory.setCategoryName(jsonObject.getString("categoryName"));
                itemCategory.setCategoryDescription(jsonObject.getString("categoryDescription"));

                if (dataset != null) {

                    dataset.add(itemCategory);
                    Log.d("response","from Json Parsing" + dataset.size());
                }
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


    void notifyDelete()
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
