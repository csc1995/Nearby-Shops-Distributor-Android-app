package org.localareadelivery.distributorapp.addStock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 20/12/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    ArrayList<Item> itemDataset = null;
    Context context;

    Items itemsActivity = null;
    //int itemCategoryID = 0;
    ItemCategory itemCategory;


    Map<Integer,ShopItem> shopItemDataset = new HashMap<>();

    final String IMAGE_ENDPOINT_URL = "/api/Images";


    public ItemsAdapter(ArrayList<Item> dataset, Context context, Items itemsActivity, ItemCategory itemCategory,
                        Map<Integer,ShopItem> shopItemDataset) {

        this.itemDataset = dataset;
        this.context = context;
        this.itemsActivity = itemsActivity;
        this.itemCategory = itemCategory;
        //makeShopItemRequest();
        this.shopItemDataset = shopItemDataset;


    }



    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       // Usual view holder initialization code
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_addstock_item_,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemsAdapter.ViewHolder holder, final int position) {

        holder.itemName.setText(itemDataset.get(position).getItemName());
        holder.itemDescription.setText(itemDataset.get(position).getItemDescription());


        if(shopItemDataset.containsKey(itemDataset.get(position).getItemID()))
        {

            holder.itemPrice.setText(
                    String.valueOf(
                            shopItemDataset.get(itemDataset.get(position).getItemID()).getItemPrice()
                    )
            );


            /*
            holder.availableItems.setText(
                    String.valueOf(
                            shopItemDataset.get(itemDataset.get(position).getItemID()).getAvailableItemQuantity()
                    )

                    );

                    */



        }

        String imagePath = getServiceURL() + IMAGE_ENDPOINT_URL + itemDataset.get(position).getItemImageURL();

        Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return itemDataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.itemName) TextView itemName;
        @Bind(R.id.itemDescription) TextView itemDescription;
        @Bind(R.id.brandName) TextView itemBrandName;

        @Bind(R.id.itemListItem)LinearLayout itemsListItem;

        //@Bind(R.id.itemPriceReduce) ImageView itemPriceReduce;
        //@Bind(R.id.itemPriceIncrease) ImageView itemPriceIncrease;
        //@Bind(R.id.itemPriceInput) EditText itemPriceInput;

        @Bind(R.id.itemImage)
        ImageView itemImage;

        //@Bind(R.id.availableItems) TextView availableItems;
        @Bind(R.id.itemPrice) TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

        }


        @OnClick(R.id.itemListItem)
        public void listItemClick()
        {

            if(itemDataset==null)
            {
                return;
            }

            if(itemDataset.size()==0)
            {
                return;
            }

            Intent intent = new Intent(context,AddStock.class);

            intent.putExtra(AddStock.ITEM_INTENT_KEY,itemDataset.get(getLayoutPosition()));

            context.startActivity(intent);


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
