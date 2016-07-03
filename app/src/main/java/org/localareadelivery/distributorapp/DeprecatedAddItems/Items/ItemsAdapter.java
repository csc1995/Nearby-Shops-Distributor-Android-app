package org.localareadelivery.distributorapp.DeprecatedAddItems.Items;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DaggerComponentBuilder;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ItemService;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopItemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 20/12/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{




    ArrayList<Item> dataset = null;
    Context context;
    private Items itemsActivity = null;
    private Map<Integer,ShopItem> shopItemDataset = null;

    private ItemCategory itemCategory;

    final String IMAGE_ENDPOINT_URL = "/api/Images";


    @Inject
    ItemService itemService;


    public ItemsAdapter(ArrayList<Item> dataset, Context context, Items itemsActivity,ItemCategory itemCategory) {

        this.dataset = dataset;
        this.context = context;
        this.itemsActivity = itemsActivity;
        this.itemCategory = itemCategory;

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

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


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.itemName) TextView itemName;
        @Bind(R.id.itemDescription) TextView itemDescription;
        @Bind(R.id.brandName) TextView itemBrandName;

        @Bind(R.id.deleteIcon) ImageView deleteIcon;
        @Bind(R.id.deleteText) TextView deleteText;
        @Bind(R.id.editIcon) ImageView editIcon;
        @Bind(R.id.editText) TextView editText;

        @Bind(R.id.itemListItem) LinearLayout itemsListItem;

        @Bind(R.id.itemImage) ImageView itemImage;


        public ViewHolder(View itemView) {

            super(itemView);

            ButterKnife.bind(this,itemView);

            //itemName = (TextView) itemView.findViewById(R.id.itemName);
            //itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            //itemBrandName = (TextView) itemView.findViewById(R.id.brandName);

            //deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);
            //deleteText = (TextView) itemView.findViewById(R.id.deleteText);

            //editIcon = (ImageView) itemView.findViewById(R.id.editIcon);
            //editText = (TextView) itemView.findViewById(R.id.editText);
        }



        @OnClick(R.id.itemListItem)
        void listItemClick(View view) {
            if (shopItemDataset != null) {

                if (shopItemDataset.containsKey(dataset.get(getLayoutPosition()).getItemID())) {

                    deleteShopItemRequest(
                            ApplicationState.getInstance().getCurrentShop().getShopID(),
                            dataset.get(getLayoutPosition()).getItemID(), this, getLayoutPosition());


                } else {

                    postShopItem(

                            ApplicationState.getInstance().getCurrentShop().getShopID(),
                            dataset.get(getLayoutPosition()).getItemID(),
                            this,
                            getLayoutPosition());

                }

            }

        }




        @OnClick({R.id.editIcon,R.id.editText})
        void editClick(View view)
        {

            Intent intent = new Intent(context,EditItem.class);

//                intent.putExtra(EditItem.ITEM_CATEGORY_INTENT_KEY,itemCategory);
            intent.putExtra(EditItem.ITEM_INTENT_KEY,dataset.get(getLayoutPosition()));

            context.startActivity(intent);

        }



        @OnClick({R.id.deleteIcon,R.id.deleteText})
        void deleteClick(View view)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Confirm Delete Item !")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            showToastMessage("Cancelled !");

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteItem();
                        }
                    })
                    .setMessage("Do you want to delete this Item ? ")
                    .show();

        }



        void deleteItem()
        {

            Call<ResponseBody> call  = itemService.deleteItem(dataset.get(getLayoutPosition()).getItemID());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.code()==200)
                    {
                        showToastMessage("Removed !");
                        notifyDelete();
                    }
                    else if(response.code() == 304)
                    {
                        showToastMessage("Not removed !");
                    }else
                    {
                        showToastMessage("Server Error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showToastMessage(context.getString(R.string.network_request_failed));
                }
            });



        }




    }// View Holder Ends



    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
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
                                                null,
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
//                    Log.d("applog","response code=" + String.valueOf(304));

                }else if(response.code() == 200)
                {
//                    Log.d("applog","response code=" + String.valueOf(200));
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

//                    Log.d("applog","response code=" + String.valueOf(201));
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
