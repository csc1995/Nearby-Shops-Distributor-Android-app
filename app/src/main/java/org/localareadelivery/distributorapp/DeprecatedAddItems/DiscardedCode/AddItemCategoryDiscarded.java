package org.localareadelivery.distributorapp.DeprecatedAddItems.DiscardedCode;

/**
 * Created by sumeet on 3/4/16.
 */
public class AddItemCategoryDiscarded {


    /*

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


    */

}
