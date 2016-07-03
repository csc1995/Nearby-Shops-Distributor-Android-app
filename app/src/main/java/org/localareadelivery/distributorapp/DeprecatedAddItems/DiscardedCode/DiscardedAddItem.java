package org.localareadelivery.distributorapp.DeprecatedAddItems.DiscardedCode;

/**
 * Created by sumeet on 3/4/16.
 */
public class DiscardedAddItem {


    /*



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





     */



}
