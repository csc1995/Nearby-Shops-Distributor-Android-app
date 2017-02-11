package org.nearbyshops.shopkeeperapp.zzDeprecatedAddStock;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.nearbyshops.shopkeeperapp.Model.ItemCategory;
import org.nearbyshops.shopkeeperapp.Model.Shop;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.shopkeeperapp.ShopHome.UtilityShopHome;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemCategories extends AppCompatActivity implements  ItemCategoriesAdapter.requestSubCategory{


    int currentCategoryID = 1; // the ID of root category is always supposed to be 1
    ItemCategory currentCategory = null;


    List<ItemCategory> dataset = new ArrayList<>();
    RecyclerView itemCategoriesList;
    ItemCategoriesAdapter listAdapter;

    GridLayoutManager layoutManager;

    Shop shop = null;


    @Bind(R.id.categoryIndicatorLabel)
    TextView categoryLabel;



    public ItemCategories() {
        super();

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstock_item_categories);



        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = (RecyclerView) findViewById(R.id.recyclerViewItemCategories);
        listAdapter = new ItemCategoriesAdapter(dataset,this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(this,1);
        itemCategoriesList.setLayoutManager(layoutManager);

        //itemCategoriesList.addItemDecoration(new DividerItemDecorationCustom(this,DividerItemDecorationCustom.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);



        layoutManager.setSpanCount(metrics.widthPixels/350);
        //layoutManager.setSpanCount();


        Log.d("applog",String.valueOf(metrics.widthPixels/250));


        if (metrics.widthPixels >= 600 && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT))
        {
            // in case of larger width of tables set the column count to 3
            //layoutManager.setSpanCount(3);
        }

    }


/*


    public void makeRequest()
    {

        String url = getServiceURL() + "/api/ItemCategory" + "?ParentID=" + currentCategoryID + "&ShopID=" + ApplicationState.getInstance().getCurrentShop().getShopID();

        Log.d("response",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("response",response);
                dataset.clear();
                parseJSON(response);
                listAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("response",error.toString());

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

*/


    public void makeRequestRetrofit()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ItemCategoryService itemCategoryService = retrofit.create(ItemCategoryService.class);




        if(UtilityShopHome.getShop(this)==null) {

            return;
        }

        // Null Pointer Exception :

            Log.d("applog","ParentID= " + String.valueOf(currentCategory.getItemCategoryID()) + " ShopID = " + String.valueOf(UtilityShopHome.getShop(this).getShopID()));

            Call<List<ItemCategory>> itemCategoryCall = itemCategoryService
                    .getItemCategories(
                            currentCategory.getItemCategoryID(),
                            UtilityShopHome.getShop(this).getShopID()
                    );



            itemCategoryCall.enqueue(new Callback<List<ItemCategory>>() {


                @Override
                public void onResponse(Call<List<ItemCategory>> call, retrofit2.Response<List<ItemCategory>> response) {


                    dataset.clear();

                    if (response.body() != null) {

                        dataset.addAll(response.body());

                        Log.d("applog","response.body()" + String.valueOf(response.body().size()) + " Dataset: " + String.valueOf(dataset.size()));
                    }

                    listAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                }
            });


    }



    public void parseJSON(String jsonString)
    {
        try {


            Gson gson = new GsonBuilder().create();

            Type listType = new TypeToken<List<ItemCategory>>() {}.getType();
            List<ItemCategory> parsedItems = gson.fromJson(jsonString,listType);

            dataset.clear();
            dataset.addAll(parsedItems);

            /*
            JSONArray array = new JSONArray(jsonString);

            for(int i=0;i<array.length();i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                ItemCategory itemCategory = new ItemCategory();
                itemCategory.setItemCategoryID(jsonObject.getInt("itemCategoryID"));
                itemCategory.setCategoryName(jsonObject.getString("categoryName"));
                itemCategory.setCategoryDescription(jsonObject.getString("categoryDescription"));
                itemCategory.setIsLeafNode(jsonObject.getBoolean("isLeafNode"));
                itemCategory.setParentCategoryID(jsonObject.getInt("parentCategoryID"));

                if (dataset != null) {


                    dataset.add(itemCategory);
                    Log.d("response","from Json Parsing" + dataset.size());
                }
            }

            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    void notifyDelete()
    {
        dataset.clear();
        makeRequestRetrofit();
        //makeRequest();

    }

    @Override
    protected void onResume() {
        super.onResume();

        shop = UtilityShopHome.getShop(this);

        // TODO
        // null pointer exception : Error Prone
        dataset.clear();
        makeRequestRetrofit();
        //makeRequest();
    }



    String categoryhash = "--";
    boolean isRootCategory = true;
    StringBuilder stringBuilder = new StringBuilder();

    ArrayList<String> categoryTree = new ArrayList<>();


    String categoryTreeString = "";




    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;

        currentCategory = itemCategory;

        currentCategoryID = itemCategory.getItemCategoryID();

        currentCategory.setParentCategory(temp);


        stringBuilder.append(categoryhash);


        categoryTree.add(new String(currentCategory.getCategoryName()));

        categoryLabel.setVisibility(View.VISIBLE);


        String hash = "--";

        if(isRootCategory == true) {


            // categoryLabel.setVisibility(View.VISIBLE);

            //categoryTreeString = stringBuilder.toString() + " " + currentCategory.getCategoryName();

            categoryTreeString = currentCategory.getCategoryName();

            categoryLabel.setText(hash + " " + categoryTreeString);



            isRootCategory = false;


        }else
        {

            categoryTreeString = "";

            boolean isFirst = true;



            for(String cat : categoryTree)
            {
                if(isFirst)
                {
                    categoryTreeString = categoryTreeString + hash + " " + cat;

                    isFirst = false;

                    hash = hash + "--";

                }else
                {
                    categoryTreeString = categoryTreeString + "\n" + hash + " " + cat;

                    hash = hash + "--";
                }

            }


            //categoryLabel.setText(categoryLabel.getText() + "\n" + stringBuilder.toString() + " " + currentCategory.getCategoryName());

            categoryLabel.setText(categoryTreeString);

        }

        makeRequestRetrofit();
        //makeRequest();
    }



    @Override
    public void onBackPressed() {

        String hash = "--";

        if(currentCategory!=null)
        {


            if(categoryTree.size()>0) {

                categoryTree.remove(categoryTree.size() - 1);
            }

            if(categoryTree.size()== 0)
            {
                categoryLabel.setVisibility(View.GONE);

                hash = "--";

            }



            categoryTreeString = "";



            boolean isFirst = true;

            for(String cat : categoryTree)
            {

                if(isFirst)
                {
                    categoryTreeString = categoryTreeString + hash + " " + cat;

                    isFirst = false;

                    hash = hash + "--";

                }else
                {
                    categoryTreeString = hash + categoryTreeString + "\n" + hash + " " + cat;

                    hash = hash + "--";
                }
            }

            //categoryLabel.setText(categoryLabel.getText() + "\n" + stringBuilder.toString() + " " + currentCategory.getCategoryName());


            categoryLabel.setText(categoryTreeString);


            if(currentCategory.getParentCategory()!=null) {

                currentCategory = currentCategory.getParentCategory();

                currentCategoryID = currentCategory.getItemCategoryID();

            }
            else
            {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            makeRequestRetrofit();
            //makeRequest();
            //moveTaskToBack(true);
        }

        if(currentCategoryID == -1)
        {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


}
