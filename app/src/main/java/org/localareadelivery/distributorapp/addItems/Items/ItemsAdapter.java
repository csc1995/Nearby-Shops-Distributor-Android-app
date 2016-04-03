package org.localareadelivery.distributorapp.addItems.Items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;


import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopItemService;
import org.localareadelivery.distributorapp.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 20/12/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    ArrayList<Item> dataset = null;
    Context context;
    Items itemsActivity = null;
    Map<Integer,ShopItem> shopItemDataset = null;

    ItemCategory itemCategory;

    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public ItemsAdapter(ArrayList<Item> dataset, Context context, Items itemsActivity,ItemCategory itemCategory) {

        this.dataset = dataset;
        this.context = context;
        this.itemsActivity = itemsActivity;
        this.itemCategory = itemCategory;

        makeShopItemRequest();
    }



    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Usual view holder initialization code
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemsAdapter.ViewHolder holder, final int position) {


        holder.itemName.setText(dataset.get(position).getItemName());
        holder.itemBrandName.setText(dataset.get(position).getBrandName());
        holder.itemDescription.setText(dataset.get(position).getItemDescription());

        String imagePath = getServiceURL() + IMAGE_ENDPOINT_URL + dataset.get(position).getItemImageURL();

        Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.itemImage);

        if(shopItemDataset!=null) {

            if (shopItemDataset.containsKey(dataset.get(position).getItemID())) {

                holder.itemsListItem.setBackgroundColor(context.getResources().getColor(R.color.itemAvailable));
            }
            else
            {
                holder.itemsListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }



        holder.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getServiceURL() + "/api/Item?itemID=" + dataset.get(position).getItemID();

                Log.d("response",url);

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);
                        notifyDelete();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("response",error.toString());
                    }
                });

                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

            }
        });


        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getServiceURL() + "/api/Item?itemID=" + dataset.get(position).getItemID();

                Log.d("response",url);

                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);
                        notifyDelete();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("response",error.toString());
                    }
                });

                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

            }

        });




        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditItem.class);

                intent.putExtra(EditItem.ITEM_CATEGORY_INTENT_KEY,itemCategory);
                intent.putExtra(EditItem.ITEM_INTENT_KEY,dataset.get(position));

                context.startActivity(intent);
            }
        });

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditItem.class);

                intent.putExtra(EditItem.ITEM_CATEGORY_INTENT_KEY,itemCategory);
                intent.putExtra(EditItem.ITEM_INTENT_KEY,dataset.get(position));

                context.startActivity(intent);
            }
        });




        holder.itemsListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shopItemDataset!=null) {

                    if (shopItemDataset.containsKey(dataset.get(position).getItemID())) {

                        deleteShopItemRequest(
                                ApplicationState.getInstance().getCurrentShop().getShopID(),
                                dataset.get(position).getItemID(),holder,position);


                    } else
                    {

                        postShopItem(

                                ApplicationState.getInstance().getCurrentShop().getShopID(),
                                dataset.get(position).getItemID(),
                                holder,
                                position);

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemName,itemDescription,itemBrandName;

        @Bind(R.id.deleteIcon) ImageView deleteIcon;
        @Bind(R.id.deleteText) TextView deleteText;
        @Bind(R.id.editIcon) ImageView editIcon;
        @Bind(R.id.editText) ImageView editText;

        @Bind(R.id.itemsListItem) LinearLayout itemsListItem;

        @Bind(R.id.itemImage) ImageView itemImage;


        public ViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemBrandName = (TextView) itemView.findViewById(R.id.brandName);

            ButterKnife.bind(this,itemView);

        }
    }




    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name),
                context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public void notifyDelete()
    {
        if(itemsActivity!=null) {
            itemsActivity.notifyDelete();
        }
    }



    public void makeShopItemRequest()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);


        // 0 for shop ID indicates get all items for the given shop
        final Call<List<ShopItem>> shopItemCall = shopItemService.getShopItems(

                                                ApplicationState.getInstance().getCurrentShop().getShopID(),
                                                0,
                                                itemCategory.getItemCategoryID());



        shopItemCall.enqueue(new Callback<List<ShopItem>>() {
            @Override
            public void onResponse(Call<List<ShopItem>> call, retrofit2.Response<List<ShopItem>> response) {

                List<ShopItem> shopItemList = response.body();

                shopItemDataset = new HashMap<Integer, ShopItem>();

                if(shopItemList!=null) {

                    for (ShopItem shopItem : shopItemList) {

                        shopItemDataset.put(shopItem.getItemID(), shopItem);
                    }
                }


                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ShopItem>> call, Throwable t) {

            }
        });

    }



    boolean isDeleted = false;

    public boolean deleteShopItemRequest(int shopID,int itemID,final ItemsAdapter.ViewHolder holder,final int position)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);

        Call<ResponseBody> responseCall = shopItemService.deleteShopItem(shopID,itemID);

        retrofit2.Response<ResponseBody> response = null;


        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.code() == 304)
                {
                    isDeleted = false;
                    Log.d("applog","response code=" + String.valueOf(304));

                }else if(response.code() == 200)
                {
                    Log.d("applog","response code=" + String.valueOf(200));
                    isDeleted = true;

                    holder.itemsListItem.setBackgroundColor(context.getResources().getColor(R.color.white));
                    shopItemDataset.remove(dataset.get(position).getItemID());

                   //notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return isDeleted;
    }



    boolean isCreated = false;


    public boolean postShopItem(int shopID,int itemID,final ItemsAdapter.ViewHolder holder, final int position){


        ShopItem shopItem = new ShopItem();

        shopItem.setItemID(itemID);
        shopItem.setShopID(shopID);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ShopItemService shopItemService = retrofit.create(ShopItemService.class);

        Call<ResponseBody> responseCall = shopItemService.postShopItem(shopItem);

        retrofit2.Response<ResponseBody> response = null;



        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if(response.code() == 201){

                    Log.d("applog","response code=" + String.valueOf(201));
                    isCreated = true;
                }else
                {
                    isCreated = false;
                }

                holder.itemsListItem.setBackgroundColor(context.getResources().getColor(R.color.itemAvailable));

                ShopItem shopItem = new ShopItem();
                shopItem.setShopID(ApplicationState.getInstance().getCurrentShop().getShopID());
                shopItem.setItemID(dataset.get(position).getItemID());

                shopItemDataset.put(shopItem.getItemID(),shopItem);

                //notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return isCreated;
    }

}
