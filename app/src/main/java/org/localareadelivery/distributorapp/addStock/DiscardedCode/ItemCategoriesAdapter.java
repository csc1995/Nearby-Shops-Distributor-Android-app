package org.localareadelivery.distributorapp.addStock.DiscardedCode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.addItems.ItemCategories.ItemCategories;

import java.util.ArrayList;

/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesAdapter extends RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder>{


    ArrayList<ItemCategory> dataset;

    Context context;
    ItemCategories itemCategories;


    public ItemCategoriesAdapter(ArrayList<ItemCategory> dataset, Context context,ItemCategories itemCategories) {
        this.dataset = dataset;
        this.context = context;
        this.itemCategories = itemCategories;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }

    @Override
    public ItemCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_addstock_item_category,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemCategoriesAdapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());


        holder.itemCategoryListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, org.localareadelivery.distributorapp.addStock.Items.class);

//                intent.putExtra(ItemCategories.ITEM_CATEGORY_INTENT_KEY,dataset.get(position));

                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private Button editButton,detachButton,deleteButton;
        private TextView categoryName,categoryDescription;
        private LinearLayout itemCategoryListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }
    }



    public void notifyDelete()
    {
//        itemCategories.notifyDelete();

    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }
}
