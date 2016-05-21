package org.localareadelivery.distributorapp.ShopList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.DAOs.ShopDAO;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.RetrofitRESTContract.ShopService;
import org.localareadelivery.distributorapp.ShopHome.ShopHome;
import org.localareadelivery.distributorapp.UtilityMethods.UtilityGeneral;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 30/12/15.
 */
public class ShopsListAdapter extends RecyclerView.Adapter<ShopsListAdapter.ViewHolder> implements ShopDAO.DeleteShopCallback{

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



    final String IMAGE_ENDPOINT_URL = "/api/Images";


    @Override
    public void onBindViewHolder(final ShopsListAdapter.ViewHolder holder, final int position) {

        holder.shopName.setText(dataset.get(position).getShopName());
        holder.shopRadius.setText(String.valueOf("Delivery Range : " + dataset.get(position).getDeliveryRange()) + "Km");

        String imagePath = UtilityGeneral.getServiceURL(context) + IMAGE_ENDPOINT_URL + dataset.get(position).getImagePath();

        Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.shopImage);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.shopName) TextView shopName;
        @Bind(R.id.radiusOfService) TextView shopRadius;
        @Bind(R.id.editIcon) ImageView editButton;
        @Bind(R.id.deleteIcon) ImageView deleteButton;
        @Bind(R.id.shopListItem) LinearLayout shopListItem;
        @Bind(R.id.shopImage)ImageView shopImage;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            shopName = (TextView) itemView.findViewById(R.id.shopName);
            shopRadius = (TextView) itemView.findViewById(R.id.radiusOfService);
            //editButton = (ImageView) itemView.findViewById(R.id.editIcon);
            //deleteButton = (ImageView) itemView.findViewById(R.id.deleteIcon);
            //shopListItem = (LinearLayout) itemView.findViewById(R.id.shopListItem);
        }



        @OnClick(R.id.shopListItem)
        public void shopListItemClick()
        {

            Intent intent = new Intent(context,ShopHome.class);
            intent.putExtra(ShopHome.SHOP_ID_INTENT_KEY,dataset.get(getLayoutPosition()).getShopID());

            Log.d("log","position= " + String.valueOf(getLayoutPosition()) + " : " + String.valueOf(dataset.size()));
            context.startActivity(intent);

            //Messy Code - To be refactored in future
            ApplicationState.getInstance().setCurrentShop(dataset.get(getLayoutPosition()));
        }


        @OnClick(R.id.editIcon)
        public void editButtonClick()
        {
            Intent intent = new Intent(context,EditShop.class);
            intent.putExtra(EditShop.INTENT_EXTRA_SHOP_KEY,dataset.get(this.getAdapterPosition()));
            Log.d("applog",dataset.get(getLayoutPosition()).toString());
            context.startActivity(intent);

        }

        @OnClick(R.id.deleteIcon)
        public void deleteButtonClick()
        {
            ShopDAO.getInstance()
                    .deleteShop(
                            dataset.get(this.getAdapterPosition()).getShopID(),
                            ShopsListAdapter.this
                    );
        }

    }


    @Override
    public void deleteShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

        if (!isOffline)
        {
            if(isSuccessful)
            {
                notifyDelete();

            }else
            {

            }

        }else
        {
            if(!isSuccessful)
            {
                Toast.makeText(context,"Application Offline ! Unable to delete Shop !",Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }





    void notifyDelete()
    {

        shopListActivity.notifyDelete();
    }

    /*

    Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UtilityGeneral.getServiceURL(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ShopService shopService = retrofit.create(ShopService.class);

            Call<ResponseBody> shopCall = shopService.deleteShop(dataset.get(this.getAdapterPosition()).getShopID());

            Log.d("applog",String.valueOf(getLayoutPosition()) + " : " + dataset.get(getLayoutPosition()).toString());


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
     */


}
