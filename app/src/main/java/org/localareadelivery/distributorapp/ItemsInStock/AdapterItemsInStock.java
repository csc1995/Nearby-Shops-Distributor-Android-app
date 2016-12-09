package org.localareadelivery.distributorapp.ItemsInStock;

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

import org.localareadelivery.distributorapp.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterItemsInStock extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

//    Map<Integer,ShopItem> shopItemMap = new HashMap<>();
//    Map<Integer,Item> selectedItems = new HashMap<>();

    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

//    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_SHOP_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;


    Fragment fragment;


    public AdapterItemsInStock(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver, Fragment fragment) {


//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_SHOP_ITEM)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_item_experimental,parent,false);
            return new ViewHolderShopItems(view);

        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_progress_bar,parent,false);

                return new LoadingViewHolder(view);

        }

//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderShopItems)
        {
            bindShopItem((ViewHolderShopItems) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }
        else if(holder instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if(fragment instanceof ItemsInStockFragment)
            {
                int fetched_count  = ((ItemsInStockFragment) fragment).fetched_items_count;
                int items_count = ((ItemsInStockFragment) fragment).item_count_item;

                if(fetched_count == items_count)
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
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        if (dataset.get(position) instanceof ShopItem)
        {
            return VIEW_TYPE_SHOP_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size()+1);
    }





    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @Bind(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolderShopItem Class declaration ends






    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }








    void bindShopItem(ViewHolderShopItems holder,int position)
    {

        if(dataset.get(position) instanceof ShopItem)
        {
//            if(dataset.size() <= position)
//            {
//                return;
//            }

            ShopItem shopItem = (ShopItem) dataset.get(position);

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




    class ViewHolderShopItems extends RecyclerView.ViewHolder{


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


        public ViewHolderShopItems(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemQuantity.addTextChangedListener(new QuantityTextWatcher());
            itemPrice.addTextChangedListener(new PriceTextWatcher());



        }


        class QuantityTextWatcher implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());

                Item item = shopItem.getItem();


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
            ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
            Item item = shopItem.getItem();

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
            ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
            Item item = shopItem.getItem();

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

                ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
                Item item = shopItem.getItem();

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
            ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
            Item item = shopItem.getItem();

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
            ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());
            Item item = shopItem.getItem();

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

                ShopItem shopItem = (ShopItem) dataset.get(getLayoutPosition());

                shopItem.setAvailableItemQuantity(quantityLocal);
                shopItem.setItemPrice(priceLocal);

                notificationReceiver.notifyShopItemUpdated((ShopItem) dataset.get(getLayoutPosition()));

            }
            else
            {
                showToastMessage("Quantity or Price not set !");
            }

        }



        @OnClick(R.id.removeButton)
        void removeClick(View view)
        {

            notificationReceiver.notifyShopItemRemoved((ShopItem) dataset.get(getLayoutPosition()));
        }

    }// View Holder Class Ends




    interface NotificationsFromAdapter
    {
        void notifyShopItemUpdated(ShopItem shopItem);
        void notifyShopItemRemoved(ShopItem shopItem);
    }



    private void showToastMessage(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

}