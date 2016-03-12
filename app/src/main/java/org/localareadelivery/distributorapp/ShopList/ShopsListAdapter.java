package org.localareadelivery.distributorapp.ShopList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ShopHome.ShopHome;

import java.util.ArrayList;

/**
 * Created by sumeet on 30/12/15.
 */
public class ShopsListAdapter extends RecyclerView.Adapter<ShopsListAdapter.ViewHolder> {

    Context context;
    ArrayList<Shop> dataset;
    Home shopListActivity;


    public ShopsListAdapter(Context context, ArrayList<Shop> dataset, Home shopListActivity) {
        this.context = context;
        this.dataset = dataset;
        this.shopListActivity = shopListActivity;
    }

    @Override
    public ShopsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_item,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ShopsListAdapter.ViewHolder holder, final int position) {

        holder.shopName.setText(dataset.get(position).getShopName());
        holder.shopRadius.setText(String.valueOf(dataset.get(position).getRadiusOfService()));

        final int positionfinal = position;

        holder.shopListItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ShopHome.class);
                intent.putExtra(ShopHome.SHOP_ID_INTENT_KEY,dataset.get(position).getShopID());
                Log.d("log","position= " + String.valueOf(position) + " : " + String.valueOf(dataset.size()));
                context.startActivity(intent);
            }

        });


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView shopName,shopRadius;
        Button editButton,deleteButton;
        LinearLayout shopListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            shopListItem = (LinearLayout) itemView.findViewById(R.id.shopListItem);
            shopName = (TextView) itemView.findViewById(R.id.shopName);
            shopRadius = (TextView) itemView.findViewById(R.id.radiusOfService);
            editButton = (Button) itemView.findViewById(R.id.editButton);

            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

        }
    }


}
