package org.localareadelivery.distributorapp.addItems.ItemCategories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.localareadelivery.distributorapp.addItems.Items.Items;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemCategoriesAdapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {



        }


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditItemCategory.class);
                intent.putExtra(EditItemCategory.ITEM_CATEGORY_ID_KEY,dataset.get(position).getItemCategoryID());


                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = getServiceURL() + "/api/ItemCategory/" + dataset.get(position).getItemCategoryID();

                StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        notifyDelete();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                VolleySingleton.getInstance(context).addToRequestQueue(request);

            }
        });

        holder.itemCategoryListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (dataset.get(position).getIsLeafNode()) {

                    Intent intent = new Intent(context, Items.class);

                    intent.putExtra(Items.ITEM_CATEGORY_ID_KEY, dataset.get(position).getItemCategoryID());
                    intent.putExtra(Items.ITEM_CATEGORY_NAME_KEY, dataset.get(position).getCategoryName());

                    context.startActivity(intent);

                }
                else
                {
                        itemCategories.notifyRequestSubCategory(dataset.get(position));
                }

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
            editButton = (Button) itemView.findViewById(R.id.editButton);
            detachButton = (Button) itemView.findViewById(R.id.detachButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }
    }



    public void notifyDelete()
    {
        itemCategories.notifyDelete();

    }


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }





    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);

    }

}
