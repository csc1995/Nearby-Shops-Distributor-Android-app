package org.nearbyshops.shopkeeperapp.QuickStockEditor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.Model.ShopItem;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterOutOfStock extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<ShopItem> dataset = null;
    private Context context;
    private NotificationReceiver notificationReceiver;

    private Fragment fragment;

    public static final int VIEW_TYPE_SHOP_ITEM = 2;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    public AdapterOutOfStock(List<ShopItem> dataset, Context context,
                             NotificationReceiver notifications,Fragment fragment) {
        this.dataset = dataset;
        this.context = context;
        this.notificationReceiver = notifications;
        this.fragment = fragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //R.layout.list_item_stock_item

        View view = null;

        if(viewType == VIEW_TYPE_SHOP_ITEM)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_shop_item_experimental,parent,false);
            return new ViewHolderShopItem(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }


        return null;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolderShopItem)
        {
            bindShopItem((ViewHolderShopItem) holder,position);
        }
        else if(holder instanceof LoadingViewHolder)
        {

            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if(fragment instanceof FragmentOutOfStock)
            {
                int items_count = ((FragmentOutOfStock) fragment).item_count;

                if(position == 0 || position == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }



    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    void bindShopItem(ViewHolderShopItem holder, int position)
    {
        if(dataset!=null)
        {
            if(dataset.size() <= position)
            {
                return;
            }

            ShopItem shopItem = dataset.get(position);

            Item item  = shopItem.getItem();


            holder.itemName.setText(item.getItemName());

//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext()) + item.getItemImageURL();

            String imagePath = UtilityGeneral.getServiceURL(context)
                    + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.nature_people, context.getTheme());

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.itemImage);


            //holder.availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " Items");
            //holder.priceText.setText("Price : " + shopItem.getItemPrice());

            holder.itemPrice.setText(String.valueOf((int)shopItem.getItemPrice()));
            holder.itemQuantity.setText(String.valueOf((int)shopItem.getAvailableItemQuantity()));

            //holder.itemPrice.setText(String.format( "%.0f", shopItem.getItemPrice()));
            //holder.itemQuantity.setText(String.format( "%.0f", shopItem.getAvailableItemQuantity()));

            if(item!=null)
            {
                holder.availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " " + item.getQuantityUnit());
                holder.priceText.setText("Price : " + shopItem.getItemPrice() + " per " + item.getQuantityUnit());

            }else
            {
                holder.availableText.setText("Available : " + shopItem.getAvailableItemQuantity() + " Items");
                holder.priceText.setText("Price : " + shopItem.getItemPrice() + " per Item");
            }


        }
    }

    class ViewHolderShopItem extends RecyclerView.ViewHolder{


        @Bind(R.id.itemName)
        TextView itemName;

//        @Bind(R.id.itemDescriptionShort)
//        TextView itemDescriptionShort;

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


        public ViewHolderShopItem(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemQuantity.addTextChangedListener(new QuantityTextWatcher());
            itemPrice.addTextChangedListener(new PriceTextWatcher());



        }


        class QuantityTextWatcher implements TextWatcher{

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Item item = dataset.get(getLayoutPosition()).getItem();


                if(!itemQuantity.getText().toString().equals(""))
                {

                    String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString()));


                    if(item!=null)
                    {
                        availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

                    }else
                    {
                        availableText.setText("Available : " + availableLocal + " Items");
                    }

                }else
                {
                    availableText.setText("Available : " + "0" + " Items");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }






        @OnClick(R.id.reduceQuantity)
        void reduceQuantityClick(View view)
        {
            Item item = dataset.get(getLayoutPosition()).getItem();

            if(!itemQuantity.getText().toString().equals(""))
            {

                if(Integer.parseInt(itemQuantity.getText().toString())<=0)
                {
                    return;
                }


                String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())-1);

                itemQuantity.setText(availableLocal);


                if(item!=null)
                {
                    availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

                }else
                {
                    availableText.setText("Available : " + availableLocal + " Items");
                }



            }else
            {
                itemQuantity.setText(String.valueOf(0));
            }

        }

        @OnClick(R.id.increaseQuantity)
        void increaseQuantityClick(View view)
        {
            Item item = dataset.get(getLayoutPosition()).getItem();

            if(!itemQuantity.getText().toString().equals(""))
            {

                String availableLocal = String.valueOf(Integer.parseInt(itemQuantity.getText().toString())+1);

                itemQuantity.setText(availableLocal);


                if(item!=null)
                {
                    availableText.setText("Available : " + availableLocal + " " + item.getQuantityUnit());

                }else
                {
                    availableText.setText("Available : " + availableLocal + " Items");
                }


            }else
            {
                itemQuantity.setText(String.valueOf(0));
            }

        }



        // methods for setting item Price



        class PriceTextWatcher implements TextWatcher{

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Item item = dataset.get(getLayoutPosition()).getItem();

                if(!itemPrice.getText().toString().equals(""))
                {

                    String priceLocal = String.valueOf(Integer.parseInt(itemPrice.getText().toString()));

                    if(item!=null)
                    {
                        priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());

                    }else
                    {
                        priceText.setText("Price : " + priceLocal + " per Item ");
                    }


                }else
                {

                    if(item!=null)
                    {
                        priceText.setText("Price : " + "0" + " per " + item.getQuantityUnit());

                    }else
                    {
                        priceText.setText("Price : " + "0" + " per Item ");
                    }

                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }



        @OnClick(R.id.reducePrice)
        void reducePriceClick(View view)
        {
            Item item = dataset.get(getLayoutPosition()).getItem();

            if(!itemPrice.getText().toString().equals(""))
            {

                if(Integer.parseInt(itemPrice.getText().toString())<=0)
                {
                    return;
                }


                String priceLocal = String.valueOf(Integer.parseInt(itemPrice.getText().toString())-1);

                itemPrice.setText(priceLocal);



                if(item!=null)
                {
                    priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());

                }else
                {
                    priceText.setText("Price : " + priceLocal + " per Item ");
                }




            }else
            {
                itemPrice.setText(String.valueOf(0));
            }

        }



        @OnClick(R.id.increasePrice)
        void increasePriceClick(View view)
        {
            Item item = dataset.get(getLayoutPosition()).getItem();

            if(!itemPrice.getText().toString().equals(""))
            {

                String priceLocal = String.valueOf(Integer.parseInt(itemPrice.getText().toString())+1);

                itemPrice.setText(priceLocal);



                if(item!=null)
                {
                    priceText.setText("Price : " + priceLocal + " per " + item.getQuantityUnit());

                }else
                {
                    priceText.setText("Price : " + priceLocal + " per Item ");
                }


            }else
            {
                itemPrice.setText(String.valueOf(0));
            }

        }






        // Update and remove methods

        @OnClick(R.id.updateButton)
        void updateClick(View view)
        {

            if(!itemQuantity.getText().toString().equals("") && !itemPrice.getText().toString().equals(""))
            {

                int quantityLocal = Integer.parseInt(itemQuantity.getText().toString());
                int priceLocal = Integer.parseInt(itemPrice.getText().toString());

                /*
                if(quantityLocal <= 0 || priceLocal<=0)
                {
                    showToastMessage("Quantity or Price not set !");

                    return;
                }
                */

                dataset.get(getLayoutPosition()).setAvailableItemQuantity(quantityLocal);
                dataset.get(getLayoutPosition()).setItemPrice(priceLocal);

                notificationReceiver.notifyShopItemUpdated(dataset.get(getLayoutPosition()));

            }
            else
            {
                showToastMessage("Quantity or Price not set !");
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
