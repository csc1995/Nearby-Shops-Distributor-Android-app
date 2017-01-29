package org.nearbyshops.shopkeeperapp.aaDeprecated_DeliveryGuy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.nearbyshops.shopkeeperapp.ModelRoles.DeliveryGuySelf;
import org.nearbyshops.shopkeeperapp.R;

import java.util.List;

/**
 * Created by sumeet on 6/6/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{


    List<DeliveryGuySelf> dataset = null;


    Context context;
    NotificationReceiver notificationReceiver;



    public Adapter(List<DeliveryGuySelf> dataset, Context context, NotificationReceiver notificationReceiver) {

        this.dataset = dataset;
        this.context = context;
        this.notificationReceiver = notificationReceiver;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_delivery_vehicle_self_item,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DeliveryGuySelf deliveryGuySelf = dataset.get(position);

        if(deliveryGuySelf != null)
        {

            holder.vehicleName.setText(deliveryGuySelf.getName());
            holder.vehicleID.setText("Vehicle ID : " + deliveryGuySelf.getDeliveryGuyID());

        }
    }



    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView vehicleName;
        TextView vehicleID;
        TextView editButton;
        TextView removeButton;
        RelativeLayout listItemDeliveryAddress;


        public ViewHolder(View itemView) {
            super(itemView);


            vehicleID = (TextView) itemView.findViewById(R.id.vehicleID);
            vehicleName = (TextView) itemView.findViewById(R.id.vehicleName);

            editButton = (TextView) itemView.findViewById(R.id.editButton);
            removeButton = (TextView) itemView.findViewById(R.id.removeButton);

            listItemDeliveryAddress = (RelativeLayout) itemView.findViewById(R.id.list_item_delivery_vehicle_self);

            editButton.setOnClickListener(this);
            removeButton.setOnClickListener(this);
            listItemDeliveryAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.editButton:

                    editClick();

                    break;

                case R.id.removeButton:

                    removeClick();

                    break;

                case R.id.list_item_delivery_vehicle_self:

                    listItemClick();

                    break;


                default:
                    break;
            }

        }


        public void listItemClick()
        {
            notificationReceiver.notifyListItemClick(dataset.get(getLayoutPosition()));
        }

        public void removeClick()
        {
            notificationReceiver.notifyRemove(dataset.get(getLayoutPosition()));
        }

        public void editClick()
        {
            notificationReceiver.notifyEdit(dataset.get(getLayoutPosition()));
        }

    }



    public interface NotificationReceiver{

        void notifyEdit(DeliveryGuySelf deliveryGuySelf);

        void notifyRemove(DeliveryGuySelf deliveryGuySelf);

        void notifyListItemClick(DeliveryGuySelf deliveryGuySelf);

    }

}
