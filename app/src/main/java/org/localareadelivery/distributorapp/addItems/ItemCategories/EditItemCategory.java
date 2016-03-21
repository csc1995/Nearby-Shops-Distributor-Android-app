package org.localareadelivery.distributorapp.addItems.ItemCategories;

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
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

public class EditItemCategory extends AppCompatActivity implements View.OnClickListener {

    EditText itemCategoryID,itemCategoryName,itemCategoryDescription;
    Button updateItemCategory;
    TextView result;

    public static final String ITEM_CATEGORY_ID_KEY = "itemCategoryIDKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemCategoryID = (EditText) findViewById(R.id.itemCategoryID);
        itemCategoryName = (EditText) findViewById(R.id.itemCategoryName);
        itemCategoryDescription = (EditText) findViewById(R.id.itemCategoryDescription);
        updateItemCategory = (Button) findViewById(R.id.updateItemCategory);
        updateItemCategory.setOnClickListener(this);

        result = (TextView) findViewById(R.id.result);

        makeGETRequest();

    }


    public void makeGETRequest()
    {

        String url = getServiceURL() + "/api/ItemCategory/" + getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0);

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


    public void makeRequest()
    {
        String url = getServiceURL() + "/api/ItemCategory/" + getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0);

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

                    jsonObject.put("categoryName",itemCategoryName.getText().toString());
                    jsonObject.put("categoryDescription",itemCategoryDescription.getText().toString());

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

            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setItemCategoryID(jsonObject.getInt("itemCategoryID"));
            itemCategory.setCategoryName(jsonObject.getString("categoryName"));
            itemCategory.setCategoryDescription(jsonObject.getString("categoryDescription"));

            //itemCategoryName.setText(itemCategory.getCategoryName());
            //itemCategoryDescription.setText(itemCategory.getCategoryDescription());

            result.setText("Result : " + "\n"
                    + itemCategory.getItemCategoryID() + "\n"
                    + itemCategory.getCategoryName() + "\n"
                    + itemCategory.getCategoryDescription() + "\n");


        } catch (JSONException e1) {

            e1.printStackTrace();
        }
    }



    public void parseGETJSON(String jsonString)
    {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            ItemCategory itemCategory = new ItemCategory();

            itemCategory.setItemCategoryID(jsonObject.getInt("itemCategoryID"));
            itemCategory.setCategoryName(jsonObject.getString("categoryName"));
            itemCategory.setCategoryDescription(jsonObject.getString("categoryDescription"));

            itemCategoryID.setText(String.valueOf(itemCategory.getItemCategoryID()));
            itemCategoryName.setText(itemCategory.getCategoryName());
            itemCategoryDescription.setText(itemCategory.getCategoryDescription());

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
            case R.id.updateItemCategory:

                makeRequest();

                break;
        }
    }
}
