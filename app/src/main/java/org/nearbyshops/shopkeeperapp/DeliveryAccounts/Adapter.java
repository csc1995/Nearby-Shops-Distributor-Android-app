package org.nearbyshops.shopkeeperapp.DeliveryAccounts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<DeliveryGuySelf> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;
    private  Context context;

    Adapter(List<DeliveryGuySelf> dataset, NotifyConfirmOrder notifyConfirmOrder, Context context) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
        this.context = context;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_delivery_guy_self,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

        DeliveryGuySelf deliveryGuy = dataset.get(position);

        holder.deliveryGuyName.setText(deliveryGuy.getName());
        holder.phoneNumber.setText(deliveryGuy.getPhoneNumber());
        holder.username.setText("username : " + deliveryGuy.getUsername());
        holder.about.setText(deliveryGuy.getAbout());


//        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);

        Drawable drawable = VectorDrawableCompat.create(context.getResources(),
                R.drawable.ic_nature_people_white_48px,context.getTheme());


        String imagePath = UtilityGeneral.getServiceURL(context) + "/api/DeliveryGuySelf/Image/" + "three_hundred_"+ deliveryGuy.getProfileImageURL() + ".jpg";

        Picasso.with(context)
                .load(imagePath)
                .placeholder(drawable)
                .into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @Bind(R.id.name)
        TextView deliveryGuyName;

        @Bind(R.id.phone_number)
        TextView phoneNumber;

        @Bind(R.id.username)
        TextView username;

        @Bind(R.id.about)
        TextView about;

        @Bind(R.id.profile_image)
        ImageView profileImage;

//        @Bind(R.id.edit_icon)
//        ImageView editIcon;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
//            itemView.setOnClickListener(this);
        }

        @OnClick({R.id.edit_icon,R.id.edit_text})
        void editClick()
        {
            notifyConfirmOrder.notifyEditClick(dataset.get(getLayoutPosition()));
        }


        @Override
        public void onClick(View v) {
//            notifyConfirmOrder.notifyListItemClick(dataset.get(getLayoutPosition()));
        }
    }




    interface NotifyConfirmOrder{

        void notifyEditClick(DeliveryGuySelf deliveryGuySelf);
//        void notifyListItemClick(DeliveryGuySelf deliveryGuySelf);
    }

}
