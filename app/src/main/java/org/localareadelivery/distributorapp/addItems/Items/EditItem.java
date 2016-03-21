package org.localareadelivery.distributorapp.addItems.Items;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

public class EditItem extends AppCompatActivity implements View.OnClickListener {

    EditText itemID,itemName,itemBrandName,itemDescription;
    Button buttonUpdateItem;
    TextView result;


    public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";
    public static final String ITEM_ID_KEY = "itemIDKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemID = (EditText) findViewById(R.id.itemID);
        itemName = (EditText) findViewById(R.id.itemName);
        itemBrandName = (EditText) findViewById(R.id.itemBrandName);
        itemDescription = (EditText) findViewById(R.id.itemDescription);

        buttonUpdateItem = (Button) findViewById(R.id.updateItemButton);
        buttonUpdateItem.setOnClickListener(this);

        result = (TextView) findViewById(R.id.result);


        makeGETRequest();
    }


    public void makeGETRequest()
    {

        String url = getServiceURL() + "/api/Item/" + getIntent().getIntExtra(ITEM_ID_KEY,0);

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseGETJSON(response);




            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response","from makeGETRequest()" + error.toString());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }



    public void parseGETJSON(String jsonString)
    {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            Item item = new Item();
            item.setItemID(jsonObject.getInt("itemID"));
            item.setItemName(jsonObject.getString("itemName"));
            item.setBrandName(jsonObject.getString("brandName"));
            item.setItemDescription(jsonObject.getString("itemDescription"));

            itemID.setText(String.valueOf(item.getItemID()));
            itemName.setText(item.getItemName());
            itemBrandName.setText(item.getBrandName());
            itemDescription.setText(item.getItemDescription());


        } catch (JSONException e1) {

            e1.printStackTrace();
        }

    }



    public void makeRequest()
    {
        String url = getServiceURL() + "/api/Item/" + getIntent().getIntExtra(ITEM_ID_KEY,0) + "?categoryID=" + getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0);

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJSON(response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public String getBodyContentType() {

                super.getBodyContentType();

                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                super.getBody();

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("itemID",itemID.getText().toString());
                    jsonObject.put("itemName",itemName.getText().toString());
                    jsonObject.put("brandName",itemBrandName.getText().toString());
                    jsonObject.put("itemDescription",itemDescription.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return jsonObject.toString().getBytes();
            }
        };


        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }


    public void parseJSON(String jsonString)
    {
        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            Item item = new Item();
            item.setItemID(jsonObject.getInt("itemID"));
            item.setItemName(jsonObject.getString("itemName"));
            item.setBrandName(jsonObject.getString("brandName"));
            item.setItemDescription(jsonObject.getString("itemDescription"));
            //itemCategoryName.setText(itemCategory.getCategoryName());
            //itemCategoryDescription.setText(itemCategory.getCategoryDescription());

            result.setText("Result : " + "\n"
                    + item.getItemID() + "\n"
                    + item.getItemName() + "\n"
                    + item.getBrandName() + "\n"
                    + item.getItemDescription());


        } catch (JSONException e1) {

            e1.printStackTrace();
        }


    }




    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);

        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.updateItemButton:

                makeRequest();

                break;
        }


    }
}
