package org.localareadelivery.distributorapp.QuickStockEditor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.ModelStats.DeliveryAddress;
import org.localareadelivery.distributorapp.ModelStats.OrderStats;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterOutOfStock extends RecyclerView.Adapter<AdapterOutOfStock.ViewHolder>{


    List<ShopItem> dataset = null;

    Context context;

    NotificationReceiver notificationReceiver;


    public AdapterOutOfStock(List<ShopItem> dataset, Context context, NotificationReceiver notifications) {
        this.dataset = dataset;
        this.context = context;
        this.notificationReceiver = notifications;

    }

    @Override
    public AdapterOutOfStock.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stock_item,parent,false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(AdapterOutOfStock.ViewHolder holder, int position) {

        if(dataset!=null)
        {
            if(dataset.size() <= position)
            {
                return;
            }

            ShopItem shopItem = dataset.get(position);

            Item item  = shopItem.getItem();


            holder.itemName.setText(item.getItemName());

            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext()) + item.getItemImageURL();

            Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.itemImage);

            holder.availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " Items");
            holder.priceText.setText("Price : " + shopItem.getItemPrice());

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.itemName)
        TextView itemName;

        @Bind(R.id.itemDescriptionShort)
        TextView itemDescriptionShort;

        @Bind(R.id.itemImage)
        ImageView itemImage;

        @Bind(R.id.availableText)
        TextView availableText;

        @Bind(R.id.reduceQuantity)
        ImageView reduceQuantity;

        @Bind(R.id.itemQuantity)
        EditText itemQuantity;

        @Bind(R.id.increaseQuantity)
        ImageView increaseQuantity;

        @Bind(R.id.priceText)
        TextView priceText;

        @Bind(R.id.reducePrice)
        ImageView reducePrice;

        @Bind(R.id.itemPrice)
        EditText itemPrice;

        @Bind(R.id.increasePrice)
        ImageView increasePrice;

        @Bind(R.id.removeButton)
        TextView removeButton;

        @Bind(R.id.updateButton)
        TextView updateButton;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

        }






        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {

            if(!itemQuantity.getText().toString().equals(""))
            {

                if(Integer.parseInt(itemQuantity.getText().toString())<=0)
                {
                    return;
                }


                String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())-1);

                itemQuantity.setText(availableLocal);

                availableText.setText("Available : " + availableLocal + " Items");



            }else
            {
                itemQuantity.setText(String.valueOf(0));
            }

        }

        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {

            if(!itemQuantity.getText().toString().equals(""))
            {

                String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())+1);

                itemQuantity.setText(availableLocal);

                availableText.setText("Available : " + availableLocal + " Items");


            }else
            {
                itemQuantity.setText(String.valueOf(0));
            }

        }



        @OnClick(R.id.updateButton)
        void updateClick(View view)
        {

            if(!itemQuantity.getText().toString().equals(""))
            {

                int quantityLocal = Integer.parseInt(itemQuantity.getText().toString());

                if(quantityLocal <= 0)
                {
                    showToastMessage("Quantity not set !");

                    return;
                }

                dataset.get(getLayoutPosition()).setAvailableItemQuantity(quantityLocal);

                notificationReceiver.notifyShopItemUpdated(dataset.get(getLayoutPosition()));

            }
            else
            {
                showToastMessage("Quantity not set !");
            }

        }



        @OnClick(R.id.removeButton)
        void removeClick(View view)
        {

            notificationReceiver.notifyShopItemRemoved(dataset.get(getLayoutPosition()));
        }



    }



    void showToastMessage(String message)
    {
        if(context!=null)
        {
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }

    }






    public interface NotificationReceiver{

        void notifyShopItemUpdated(ShopItem shopItem);
        void notifyShopItemRemoved(ShopItem shopItem);

    }

}
