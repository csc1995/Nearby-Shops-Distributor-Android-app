package org.localareadelivery.distributorapp.addItems.DiscardedCode;

/**
 * Created by sumeet on 3/4/16.
 */
public class DiscardedEditItem {

    /*



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
        String url = getServiceURL() + "/api/Item/" + getIntent().getIntExtra(ITEM_ID_KEY,0);

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

            // + "?categoryID=" + getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0)

            @Override
            public byte[] getBody() throws AuthFailureError {

                super.getBody();

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("itemID",itemID.getText().toString());
                    jsonObject.put("itemName",itemName.getText().toString());
                    jsonObject.put("brandName",itemBrandName.getText().toString());
                    jsonObject.put("itemDescription",itemDescription.getText().toString());
                    jsonObject.put("itemCategoryID",getIntent().getIntExtra(ITEM_CATEGORY_ID_KEY,0));


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



     */


}
