package org.localareadelivery.distributorapp.DeprecatedAddItems.ItemCategories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.localareadelivery.distributorapp.Utility.UtilityGeneral;
import org.localareadelivery.distributorapp.DeprecatedAddItems.Items.Items;
import org.localareadelivery.distributorapp.Model.ItemCategory;
import org.localareadelivery.distributorapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class ItemCategoriesAdapter extends RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder>{


    List<ItemCategory> dataset;

    Context context;
    ItemCategories itemCategories;

    final String IMAGE_ENDPOINT_URL = "/api/Images";

    public ItemCategoriesAdapter(List<ItemCategory> dataset, Context context, ItemCategories activity) {

        this.dataset = dataset;
        this.context = context;
        this.itemCategories = activity;

        if(this.dataset == null)
        {
            this.dataset = new ArrayList<ItemCategory>();
        }

    }

    @Override
    public ItemCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category_deprecated,parent,false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ItemCategoriesAdapter.ViewHolder holder, final int position) {

        holder.categoryName.setText(dataset.get(position).getCategoryName());
        holder.categoryDescription.setText(dataset.get(position).getCategoryDescription());


        String imagePath = UtilityGeneral.getImageEndpointURL(context)
                + dataset.get(position).getImagePath();

        Picasso.with(context).load(imagePath).placeholder(R.drawable.nature_people).into(holder.categoryImage);

        Log.d("applog",imagePath);

    }


    @Override
    public int getItemCount() {

        return dataset.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private Button editButton;

        Button detachButton;

        Button deleteButton;

        private TextView categoryName,categoryDescription;
        @Bind(R.id.itemCategoryListItem) LinearLayout itemCategoryListItem;
        @Bind(R.id.categoryImage) ImageView categoryImage;

        @Bind(R.id.deleteIcon) ImageView deleteIcon;
        @Bind(R.id.editIcon) ImageView editIcon;
        @Bind(R.id.textviewEdit) TextView textViewEdit;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            categoryDescription = (TextView) itemView.findViewById(R.id.categoryDescription);

            itemCategoryListItem = (LinearLayout) itemView.findViewById(R.id.itemCategoryListItem);
        }



        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {

            if (dataset == null) {

                return;
            }

            if(dataset.size()==0)
            {
                return;
            }

            if (dataset.get(getLayoutPosition()).getIsLeafNode()) {

                Intent intent = new Intent(context, Items.class);

                intent.putExtra(Items.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));

                context.startActivity(intent);

            }
            else
            {

                itemCategories.notifyRequestSubCategory(dataset.get(getLayoutPosition()));
            }


        }








        @OnClick(R.id.deleteIcon)
        void deleteClick(View view)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Confirm Delete Item Category !")
                    .setMessage("Do you want to delete this Item Category ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //deleteItemCategory();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            showToastMessage("Cancelled !");
                        }
                    })
                    .show();


        }




       /* public void deleteItemCategory()
        {


            String url = UtilityGeneral.getServiceURL(context) + "/api/ItemCategory/" + dataset.get(getLayoutPosition()).getItemCategoryID();

            StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    notifyDelete();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context,"Delete failed !",Toast.LENGTH_SHORT).show();

                }
            });

            VolleySingleton.getInstance(context).addToRequestQueue(request);

        }

*/
        @OnClick({R.id.editIcon,R.id.textviewEdit})
        public void editIcon()
        {
            Intent intent = new Intent(context,EditItemCategory.class);
            intent.putExtra(EditItemCategory.ITEM_CATEGORY_INTENT_KEY,dataset.get(getLayoutPosition()));
            context.startActivity(intent);

        }

    }// ViewHolder Class declaration ends




    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    public void notifyDelete()
    {
        itemCategories.notifyDelete();

    }


    public interface requestSubCategory
    {
        // method for notifying the list object to request sub category
        public void notifyRequestSubCategory(ItemCategory itemCategory);
    }



}