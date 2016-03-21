package org.localareadelivery.distributorapp.addRemoveItems.categories_items;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class AddItemCategory extends AppCompatActivity implements View.OnClickListener {


    EditText itemCategoryName,itemCategoryDescription;
    Button addItemCategory;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoryName = (EditText) findViewById(R.id.itemCategoryName);
        itemCategoryDescription = (EditText) findViewById(R.id.itemCategoryDescription);
        addItemCategory = (Button) findViewById(R.id.addItemCategory);
        addItemCategory.setOnClickListener(this);
        result = (TextView) findViewById(R.id.result);

    }



    public void makeRequest()
    {
        String url = getServiceURL() + "/api/ItemCategory";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

            result.setText("Result : " + "\n"
            + itemCategory.getItemCategoryID() + "\n"
            + itemCategory.getCategoryName() + "\n"
            + itemCategory.getCategoryDescription() + "\n");


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
            case R.id.addItemCategory:

                makeRequest();

                break;
        }
    }
}
