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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopItemService;
import org.localareadelivery.distributorapp.addItems.Items.EditItem;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        holder.itemBrandName.setText(itemDataset.get(position).getBrandName());
        holder.itemDescription.setText(itemDataset.get(position).getItemDescription());


        if(shopItemDataset.containsKey(itemDataset.get(position).getItemID()))
        {

            holder.itemPrice.setText(
                    String.valueOf(
                            shopItemDataset.get(itemDataset.get(position).getItemID()).getItemPrice()
                    )
            );

            holder.availableItems.setText(
                    String.valueOf(
                            shopItemDataset.get(itemDataset.get(position).getItemID()).getAvailableItemQuantity()
                    )

            );

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

        @Bind(R.id.itemPriceReduce) ImageView itemPriceReduce;
        @Bind(R.id.itemPriceIncrease) ImageView itemPriceIncrease;
        @Bind(R.id.itemPriceInput) EditText itemPriceInput;

        @Bind(R.id.itemImage)
        ImageView itemImage;

        @Bind(R.id.availableItems) TextView availableItems;
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


        @OnClick(R.id.itemPriceIncrease)
        public void setItemPriceIncrease(View view)
        {

              //Toast.makeText(context,"Position: " + getLayoutPosition() ,Toast.LENGTH_SHORT).show();

              int price = 0;


            if(itemPriceInput.getText().toString() != "") {

                price = Integer.parseInt(itemPriceInput.getText().toString()) + 1;

                itemPriceInput.setText(String.valueOf(price));
            }

        }



        @OnClick(R.id.itemPriceReduce)
        public void setItemPriceReduce(View view)
        {

            //Toast.makeText(context,"Position: " + getLayoutPosition() ,Toast.LENGTH_SHORT).show();

            int price = 0;

            try {
                price = Integer.parseInt(itemPriceInput.getText().toString()) - 1;

                itemPriceInput.setText(String.valueOf(price));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

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
