package org.localareadelivery.distributorapp.Items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by sumeet on 20/12/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    ArrayList<Item> dataset;
    Context context;
    Items itemsActivity;


    public ItemsAdapter(ArrayList<Item> dataset, Context context, Items itemsActivity) {
        this.dataset = dataset;
        this.context = context;
        this.itemsActivity = itemsActivity;
    }

    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_items_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, final int position) {

        holder.itemName.setText(dataset.get(position).getItemName());
        holder.itemBrandName.setText(dataset.get(position).getBrandName());
        holder.itemDescription.setText(dataset.get(position).getItemDescription());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
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

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditItem.class);
                intent.putExtra(EditItem.ITEM_ID_KEY,dataset.get(position).getItemID());
                intent.putExtra(EditItem.ITEM_CATEGORY_ID_KEY,itemsActivity.getIntent().getIntExtra(Items.ITEM_CATEGORY_ID_KEY,0));

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName,itemDescription,itemBrandName;
        private Button editButton,detachButton,deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.itemName);
            itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemBrandName = (TextView) itemView.findViewById(R.id.brandName);


            editButton = (Button) itemView.findViewById(R.id.editButton);
            detachButton = (Button) itemView.findViewById(R.id.detachButton);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);
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
