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

public class AddItem extends AppCompatActivity implements View.OnClickListener {


    public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";

    EditText itemName,brandName,itemDescription;
    Button addItemButton;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemName = (EditText) findViewById(R.id.itemName);
        brandName = (EditText) findViewById(R.id.brandName);
        itemDescription = (EditText) findViewById(R.id.itemDescription);

        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(this);
        result = (TextView) findViewById(R.id.result);


    }


    public void makeRequest()
    {
        String url = getServiceURL() + "/api/Item?categoryID=" + String.valueOf(getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0));

        Log.d("response",url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);

                parseJSON(response);

  //              itemsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());
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

                    jsonObject.put("itemName",itemName.getText().toString());
                    jsonObject.put("itemDescription",itemDescription.getText().toString());
                    jsonObject.put("brandName",brandName.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return jsonObject.toString().getBytes();
            }
        };;

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    public void parseJSON(String jsonString)
    {
        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            Item item = new Item();
            item.setItemName(jsonObject.getString("itemName"));
            item.setItemID(jsonObject.getInt("itemID"));
            item.setItemDescription(jsonObject.getString("itemDescription"));
            item.setBrandName(jsonObject.getString("brandName"));


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
            case R.id.addItemButton:

                makeRequest();

                break;
        }
    }
}
