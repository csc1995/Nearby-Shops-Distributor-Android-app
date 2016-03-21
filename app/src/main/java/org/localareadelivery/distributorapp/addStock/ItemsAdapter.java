package org.localareadelivery.distributorapp.addStock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopItemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 20/12/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    ArrayList<Item> itemDataset = null;
    Context context;
    Items itemsActivity = null;
    //int itemCategoryID = 0;
    ItemCategory itemCategory;


    Map<Integer,ShopItem> shopItemDataset = null;


    public ItemsAdapter(ArrayList<Item> dataset, Context context, Items itemsActivity, ItemCategory itemCategory) {
        this.itemDataset = dataset;
        this.context = context;
        this.itemsActivity = itemsActivity;
        this.itemCategory = itemCategory;
        //makeShopItemRequest();

    }



    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       // Usual view holder initialization code
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_items_list_addstock,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemsAdapter.ViewHolder holder, final int position) {

        holder.itemName.setText(itemDataset.get(position).getItemName());
        holder.itemBrandName.setText(itemDataset.get(position).getBrandName());
        holder.itemDescription.setText(itemDataset.get(position).getItemDescription());

        holder.itemsListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context,AddStock.class);

                intent.putExtra(AddStock.ITEM_INTENT_KEY,itemDataset.get(position));

                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return itemDataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.itemName) TextView itemName;
        @Bind(R.id.itemDescription) TextView itemDescription;
        @Bind(R.id.brandName) TextView itemBrandName;

        @Bind(R.id.itemsListItem)LinearLayout itemsListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

        }
    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }


    public void notifyDelete()
    {
        if(itemsActivity!=null) {
            itemsActivity.notifyDelete();
        }
    }

}
