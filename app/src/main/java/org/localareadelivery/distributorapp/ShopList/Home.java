package org.localareadelivery.distributorapp.ShopList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.localareadelivery.distributorapp.DividerItemDecoration;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {


    ArrayList<Shop> dataset = new ArrayList<>();

    RecyclerView shopList;
    ShopsListAdapter shopListAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                //Intent intent = new Intent(Home.this,AddShop.class);

                startActivity(new Intent(Home.this,AddShop.class));

            }
        });


        shopList = (RecyclerView) findViewById(R.id.shopsList);
        shopListAdapter = new ShopsListAdapter(this,dataset,this);
        shopList.setAdapter(shopListAdapter);
        layoutManager = new GridLayoutManager(null,1);
        shopList.setLayoutManager(layoutManager);
        shopList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));



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



    public void makeRequest()
    {

        String url = getServiceURL() + "/api/Shop" + "?DistributorID=" + String.valueOf(getDistributorID());

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);
                parseJSON(response);
                shopListAdapter.notifyDataSetChanged();

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


                Shop shop = new Shop();
                shop.setShopID(jsonObject.getInt("shopID"));
                shop.setShopName(jsonObject.getString("shopName"));
                shop.setRadiusOfService(jsonObject.getInt("radiusOfService"));


                if (dataset != null) {

                    dataset.add(shop);
                    Log.d("response","from Json Parsing" + dataset.size());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public String  getServiceURL()
    {
        // Get a handle to the shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public int getDistributorID()
    {
        // Get a handle to shared preference
        SharedPreferences sharedPref;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        // read from shared preference
        int distributorID = sharedPref.getInt(getString(R.string.preference_distributor_id_key),0);

        return distributorID;
    }


    @Override
    protected void onResume() {
        super.onResume();

        dataset.clear();
        makeRequest();
    }


}
