package org.localareadelivery.distributorapp.addStock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.localareadelivery.distributorapp.ApplicationState.ApplicationState;
import org.localareadelivery.distributorapp.Model.Item;
import org.localareadelivery.distributorapp.Model.Shop;
import org.localareadelivery.distributorapp.Model.ShopItem;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.ServiceContract.ShopItemService;
import org.localareadelivery.distributorapp.ServiceContract.ShopService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import okhttp3.internal.http.StatusLine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

public class AddStock extends AppCompatActivity {


    final static String ITEM_INTENT_KEY = "itemIntentKey";

    Item item = null;

    @Bind(R.id.availableItems) TextView availableItems;
    @Bind(R.id.itemPrice) TextView itemPrice;
    @Bind(R.id.results) TextView results;
    @Bind(R.id.itemName) TextView itemName;

    @Bind(R.id.updateStockButton)Button updateStock;

    @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    ShopItem shopItemResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item = getIntent().getParcelableExtra(ITEM_INTENT_KEY);

        Shop currentShop = ApplicationState.getInstance().getCurrentShop();

        if(item!=null && currentShop!=null) {

            itemName.setText(item.getItemName());
            makeGETRequest(currentShop.getShopID(), item.getItemID());

        }

    }


    @OnClick(R.id.updateStockButton)
    public void updateStock()
    {

        ShopItem shopItem = new ShopItem();

        shopItem.setItemID(item.getItemID());
        shopItem.setShopID(ApplicationState.getInstance().getCurrentShop().getShopID());

        shopItem.setAvailableItemQuantity(Integer.parseInt(availableItems.getText().toString()));
        shopItem.setItemPrice(Double.parseDouble(itemPrice.getText().toString()));



        makePOSTRequest(shopItem);


    }







    boolean requestStatus;

    public boolean makePOSTRequest(final ShopItem shopDetails)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);


        Call<ResponseBody> shopItemCall = shopItemService.putShopItem(shopDetails);

        shopItemCall.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    // posted and created successfully

                    requestStatus = true;

                    snackbarMessage("Stock Updated Successfully!");

                    makeGETRequest(shopDetails.getShopID(),shopDetails.getItemID());



                }else if (response.code()== 304)
                {
                    // not modified

                    snackbarMessage("Stock addition unsuccessfull!");
                    requestStatus = false;


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



        return requestStatus;
    }


    public void makeGETRequest(int shopID, int itemID)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getServiceURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopItemService shopItemService = retrofit.create(ShopItemService.class);


        Call<List<ShopItem>> shopItemCall = shopItemService.getShopItems(shopID,itemID,0);

        shopItemCall.enqueue(new Callback<List<ShopItem>>() {
            @Override
            public void onResponse(Call<List<ShopItem>> call, Response<List<ShopItem>> response) {

                List<ShopItem> shopItemList = response.body();

//                if (shopItemList.size() == 1)
  //              {

                    updateResult(shopItemList.get(0));
    //            }


            }

            @Override
            public void onFailure(Call<List<ShopItem>> call, Throwable t) {

            }
        });



    }


    public void updateResult(ShopItem shopItem)
    {
        String resultText = "Result : " + "\n"
                + "\n" + "Shop ID : " + shopItem.getShopID()
                + "\n" + "Item ID : " + shopItem.getItemID()
                + "\n" + "Available Quantity : "  + shopItem.getAvailableItemQuantity()
                + "\n" + "Item Price : " + shopItem.getItemPrice();


        availableItems.setText(String.valueOf(shopItem.getAvailableItemQuantity()));

        itemPrice.setText(String.valueOf(shopItem.getItemPrice()));

        results.setText(resultText);

    }


    public void snackbarMessage(String message)
    {
        Snackbar.make(coordinatorLayout,message, Snackbar.LENGTH_LONG)
              .setAction("Action", null).show();
    }








    public String  getServiceURL()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_name), this.MODE_PRIVATE);
        String service_url = sharedPref.getString(getString(R.string.preference_service_url_key),"default");

        return service_url;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


        ButterKnife.unbind(this);

    }
}