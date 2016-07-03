package org.localareadelivery.distributorapp.DeprecatedAddItems.DiscardedCode;

/**
 * Created by sumeet on 3/4/16.
 */
public class ItemsDiscarded {

    /*


    public void makeRequest()
    {
        String url = getServiceURL() + "/api/Item?ItemCategoryID=" + String.valueOf(itemCategory.getItemCategoryID());

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


     */
}
