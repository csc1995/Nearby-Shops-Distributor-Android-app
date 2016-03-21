package org.localareadelivery.distributorapp.ShopList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;
import org.localareadelivery.distributorapp.ShopHome.ShopHome;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop_list,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ShopsListAdapter.ViewHolder holder, final int position) {

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


                //Messy Code - To be refactored in future

                ApplicationState.getInstance().setCurrentShop(dataset.get(position));

            }

        });




        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditShop.class);
                intent.putExtra(EditShop.INTENT_EXTRA_SHOP_KEY,dataset.get(holder.getAdapterPosition()));
                Log.d("applog",dataset.get(position).toString());
                context.startActivity(intent);

            }
        });


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getServiceURL())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ShopService shopService = retrofit.create(ShopService.class);

                Call<ResponseBody> shopCall = shopService.deleteShop(dataset.get(holder.getAdapterPosition()).getShopID());

                Log.d("applog",String.valueOf(position) + " : " + dataset.get(position).toString());


                shopCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.isSuccessful()) {
                            notifyDelete();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

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


    public String  getServiceURL()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_name), context.MODE_PRIVATE);

        String service_url = sharedPref.getString(context.getString(R.string.preference_service_url_key),"default");

        return service_url;
    }

    void notifyDelete()
    {

        shopListActivity.notifyDelete();

    }


}
