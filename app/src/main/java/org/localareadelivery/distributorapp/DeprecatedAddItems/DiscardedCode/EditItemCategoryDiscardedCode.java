package org.localareadelivery.distributorapp.DeprecatedAddItems.DiscardedCode;

/**
 * Created by sumeet on 2/4/16.
 */
public class EditItemCategoryDiscardedCode {



    /*


        public void retrofitGETRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);


        if(itemCategoryForEdit!=null) {

            Call<ItemCategory> categoryCall = itemCategoryService.getItemCategory(itemCategoryForEdit.getItemCategoryID());


            categoryCall.enqueue(new Callback<ItemCategory>() {

                @Override
                public void onResponse(Call<ItemCategory> call, retrofit2.Response<ItemCategory> response) {

                    ItemCategory itemCategory = response.body();

                    if(itemCategory!=null)
                    {

                    }

                }

                @Override
                public void onFailure(Call<ItemCategory> call, Throwable t) {

                }
            });

        }


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

    */


}
